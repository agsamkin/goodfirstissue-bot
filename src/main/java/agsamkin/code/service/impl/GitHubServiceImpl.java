package agsamkin.code.service.impl;

import agsamkin.code.exception.GHLanguageGettingException;
import agsamkin.code.exception.GHIssueGettingException;
import agsamkin.code.exception.GHRateLimitException;
import agsamkin.code.exception.GHRateLimitGettingException;
import agsamkin.code.exception.GHRepoGettingException;
import agsamkin.code.exception.GHRepoLanguagesGettingException;
import agsamkin.code.model.Language;
import agsamkin.code.service.GitHubService;
import agsamkin.code.util.GitHubUtil;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

import lombok.RequiredArgsConstructor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.kohsuke.github.GHDirection;
import org.kohsuke.github.GHFork;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueQueryBuilder;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.RateLimitTarget;

import org.springframework.stereotype.Service;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static agsamkin.code.util.GitHubUtil.GITHUB_LANGUAGES_URL;
import static agsamkin.code.util.GitHubUtil.GOOD_FIRST_ISSUE_LABEL_FILTER;
import static agsamkin.code.util.GitHubUtil.MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT;
import static agsamkin.code.util.GitHubUtil.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;
import static agsamkin.code.util.GitHubUtil.MIN_FORKS_REPO_FILTER;
import static agsamkin.code.util.GitHubUtil.MIN_GOOD_FIRST_ISSUES_FILTER;
import static agsamkin.code.util.GitHubUtil.MIN_STARS_REPO_FILTER;
import static agsamkin.code.util.GitHubUtil.QUERY_REPOS;
import static org.kohsuke.github.RateLimitTarget.CORE;
import static org.kohsuke.github.RateLimitTarget.SEARCH;

@RequiredArgsConstructor
@Service
public class GitHubServiceImpl implements GitHubService {
    private final GitHub gitHub;
    private final GitHubUtil gitHubUtil;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Set<String> getAllLanguages() throws GHLanguageGettingException {
        Set<String> languages = new TreeSet<>();

        HttpResponse<String> response = Unirest.get(GITHUB_LANGUAGES_URL).asString();
        if (response.getStatus() != HttpStatus.OK) {
            throw new GHLanguageGettingException("Languages parsing error");
        }

        final String searchLanguageElement = "search_language";
        final String languageNodeName = "option";

        String body = response.getBody();
        Document doc = Jsoup.parse(body);

        Element searchLanguage = doc.getElementById(searchLanguageElement);
        Elements elements = searchLanguage.getAllElements();

        for (Element element : elements) {
            if (languageNodeName.equals(element.nodeName())) {
                String languageName = element.ownText();
                languages.add(languageName);
            }
        }

        return languages;
    }

    @Override
    public Map<String, Long> getRepoLanguages(GHRepository ghRepo) throws GHRepoLanguagesGettingException, GHRateLimitException {
        checkRateLimit(CORE);
        try {
            return ghRepo.listLanguages();
        } catch (IOException e) {
            throw new GHRepoLanguagesGettingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public GHRepository getRepoByRepoId(Long repoId) throws GHRepoGettingException, GHRateLimitException {
        checkRateLimit(CORE);
        try {
            return gitHub.getRepositoryById(repoId);
        } catch (IOException e) {
            throw new GHRepoGettingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<GHRepository> getReposByLanguage(Language language, Date updatedFrom) throws GHRateLimitException {
        checkRateLimit(SEARCH);
        return gitHub.searchRepositories()
                .visibility(GHRepository.Visibility.PUBLIC)
                .fork(GHFork.PARENT_ONLY)
                .language("\"%s\"".formatted(language.getName()))
                .pushed(">=%s".formatted(formatter.format(updatedFrom)))
                .stars(">=%s".formatted(MIN_STARS_REPO_FILTER))
                .q(QUERY_REPOS.formatted(MIN_FORKS_REPO_FILTER, MIN_GOOD_FIRST_ISSUES_FILTER))
                .sort(GHRepositorySearchBuilder.Sort.UPDATED)
                .order(GHDirection.DESC)
                .list().withPageSize(MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT)
                .iterator().nextPage();
    }

    @Override
    public List<GHRepository> getReposByLanguage(Language language) throws GHRateLimitException {
        checkRateLimit(SEARCH);
        return getReposByLanguage(language, gitHubUtil.getLastDateUpdatedRepoFilter());
    }

    @Override
    public GHIssue getIssueFromRepoByNumber(GHRepository ghRepo, Integer number) throws GHIssueGettingException, GHRateLimitException {
        checkRateLimit(CORE);
        try {
            return ghRepo.getIssue(number);
        } catch (IOException e) {
            throw new GHIssueGettingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<GHIssue> getIssuesByRepo(GHRepository ghRepo, Date since) throws GHRateLimitException {
        checkRateLimit(CORE);
        return ghRepo.queryIssues()
                .state(GHIssueState.OPEN)
                .since(since)
                .label(GOOD_FIRST_ISSUE_LABEL_FILTER)
                .sort(GHIssueQueryBuilder.Sort.UPDATED)
                .direction(GHDirection.DESC)
                .list().withPageSize(MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT)
                .iterator().nextPage();
    }

    @Override
    public List<GHIssue> getIssuesByRepo(GHRepository ghRepo) throws GHRateLimitException {
        checkRateLimit(CORE);
        return getIssuesByRepo(ghRepo, gitHubUtil.getLastDateUpdatedIssueFilter());
    }

    @Override
    public GHRateLimit getRateLimit() throws GHRateLimitGettingException {
        try {
            return gitHub.getRateLimit();
        } catch (IOException e) {
            throw new GHRateLimitGettingException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void checkRateLimit(RateLimitTarget rateLimitTarget) throws GHRateLimitException {
        GHRateLimit ghRateLimit;
        try {
            ghRateLimit = getRateLimit();
        } catch (GHRateLimitGettingException e) {
            throw new GHRateLimitException(e.getMessage(), e.getCause());
        }

        if (!gitHubUtil.checkRateLimitByTarget(ghRateLimit, rateLimitTarget)) {
            throw new GHRateLimitException("Request rate limit exceeded");
        }
    }
}
