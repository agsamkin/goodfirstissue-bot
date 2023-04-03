package agsamkin.code.service.impl;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import agsamkin.code.model.repo.Repo;
import agsamkin.code.model.repo.Visibility;
import agsamkin.code.service.GitHubService;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;
import kong.unirest.Unirest;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.apache.commons.lang3.time.DateUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Transactional
@RequiredArgsConstructor
@Service
public class GitHubServiceImpl implements GitHubService {
    public static final String GITHUB_LANGUAGES_URL = "https://github.com/search/advanced";

    public static final String QUERY_REPOS = "archived:false template:false good-first-issues:>=1";
    public static final Date LAST_DATE_UPDATED_REPO_FILTER = DateUtils.addMonths(new Date(), -1);
    public static final int MIN_STARS_REPO_FILTER = 1;

    public static final Date LAST_DATE_UPDATED_ISSUE_FILTER = DateUtils.addMonths(new Date(), -3);
    public static final String GOOD_FIRST_ISSUE_LABEL_FILTER = "good first issue";

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final GitHub gitHub;

    @Transactional(readOnly = true)
    @Override
    public List<Language> getAllLanguages() {
        List<Language> languages = new ArrayList<>();

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
                Language language = Language.builder()
                        .name(languageName).build();
                languages.add(language);
            }
        }

        languages.sort(Comparator.comparing(Language::getName));
        return languages;
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Override
    public List<Repo> getReposWithGoodFirstIssuesByLanguageFromDate(Language language, Date repoFrom, Date issueFrom) {
        List<Repo> repos = new ArrayList<>();

        List<GHRepository> searchRepos = gitHub.searchRepositories()
                .visibility(GHRepository.Visibility.PUBLIC)
                .fork(GHFork.PARENT_ONLY)
                .language("\"%s\"".formatted(language.getName()))
                .pushed(">=%s".formatted(formatter.format(LAST_DATE_UPDATED_REPO_FILTER)))
                .stars(">=%s".formatted(MIN_STARS_REPO_FILTER))
                .q(QUERY_REPOS)
                .sort(GHRepositorySearchBuilder.Sort.UPDATED)
                .order(GHDirection.DESC)
                .list().withPageSize(100).iterator().nextPage();

        for (GHRepository searchRepo : searchRepos) {

            if (searchRepo.getId() != 1223855L) {
                continue;
            }

            Repo repo = Repo.builder()
                    .repoId(searchRepo.getId())
                    .visibility(Visibility.valueOf(searchRepo.getVisibility().name()))
                    .archived(searchRepo.isArchived())
                    .isTemplate(searchRepo.isTemplate())
                    .name(searchRepo.getName())
                    .fullName(searchRepo.getFullName())
                    .description(searchRepo.getDescription())
                    .htmlUrl(searchRepo.getHtmlUrl().toString())
                    .url(searchRepo.getUrl().toString())
                    .forksCount(searchRepo.getForksCount())
                    .stargazersCount(searchRepo.getStargazersCount())
                    .watchersCount(searchRepo.getWatchersCount())
                    .createdAt(searchRepo.getCreatedAt())
                    .updatedAt(searchRepo.getUpdatedAt())
                    .pushedAt(searchRepo.getPushedAt())
                    .language(language).build();

            List<GHIssue> searchIssues = searchRepo.queryIssues()
                    .state(GHIssueState.OPEN)
                    .since(LAST_DATE_UPDATED_ISSUE_FILTER)
                    .label(GOOD_FIRST_ISSUE_LABEL_FILTER)
                    .sort(GHIssueQueryBuilder.Sort.UPDATED)
                    .direction(GHDirection.DESC)
                    .list().withPageSize(20).iterator().nextPage();

            List<Issue> issues = new ArrayList<>();
            for (GHIssue searchIssue : searchIssues) {
                if (searchIssue.isLocked() || searchIssue.isPullRequest()) {
                    continue;
                }

                Issue issue = Issue.builder()
                        .issueId(searchIssue.getId())
                        .number(searchIssue.getNumber())
                        .title(searchIssue.getTitle())
                        .htmlUrl(searchIssue.getHtmlUrl().toString())
                        .url(searchIssue.getUrl().toString())
                        .locked(searchIssue.isLocked())
                        .commentsCount(searchIssue.getCommentsCount())
                        .createdAt(searchIssue.getCreatedAt())
                        .updatedAt(searchIssue.getUpdatedAt())
                        .closedAt(searchIssue.getClosedAt())
                        .repo(repo).build();

                issues.add(issue);
            }

            if (issues.size() == 0) {
                continue;
            }

            repo.setIssues(issues);
            repos.add(repo);
        }

        return repos;
    }

    @Override
    public List<Repo> getReposWithGoodFirstIssuesByLanguage(Language language) {
        return getReposWithGoodFirstIssuesByLanguageFromDate(
                language, LAST_DATE_UPDATED_REPO_FILTER, LAST_DATE_UPDATED_ISSUE_FILTER);
    }
}
