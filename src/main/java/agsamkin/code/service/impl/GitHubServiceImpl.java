package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.service.GitHubService;

import agsamkin.code.util.GitHubUtil;
import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.kohsuke.github.GHDirection;
import org.kohsuke.github.GHFork;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueQueryBuilder;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RequiredArgsConstructor
@Service
public class GitHubServiceImpl implements GitHubService {
    public static final String GITHUB_LANGUAGES_URL = "https://github.com/search/advanced";

    public static final String QUERY_REPOS =
            "archived:false template:false mirror:false forks:>=%s good-first-issues:>=%s";

    public static final int MIN_STARS_REPO_FILTER = 5;
    public static final int MIN_FORKS_REPO_FILTER = 5;
    public static final int MIN_GOOD_FIRST_ISSUES_FILTER = 1;
    public static final int MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT = 100;

    public static final String GOOD_FIRST_ISSUE_LABEL_FILTER = "good first issue";
    public static final int MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT = 20;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private final GitHub gitHub;
    private final GitHubUtil gitHubUtil;

    @Override
    public Set<String> getAllLanguages() {
        Set<String> languages = new TreeSet<>();

        HttpResponse<String> response = Unirest.get(GITHUB_LANGUAGES_URL).asString();
        if (response.getStatus() != HttpStatus.OK) {
            return null;
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

    @SneakyThrows
    @Override
    public GHRepository getRepoByRepoId(Long repoId) {
        return gitHub.getRepositoryById(repoId);
    }

    @Override
    public List<GHRepository> getReposByLanguage(Language language, Date updatedFrom) {
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
    public List<GHRepository> getReposByLanguage(Language language) {
        return getReposByLanguage(language, gitHubUtil.getLastDateUpdatedRepoFilter());
    }

    @SneakyThrows
    @Override
    public GHIssue getIssueFromRepoByNumber(GHRepository ghRepo, Integer number) {
        return ghRepo.getIssue(number);
    }

    @Override
    public List<GHIssue> getIssuesByRepo(GHRepository ghRepo, Date since) {
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
    public List<GHIssue> getIssuesByRepo(GHRepository ghRepo) {
        return getIssuesByRepo(ghRepo, gitHubUtil.getLastDateUpdatedIssueFilter());
    }
}
