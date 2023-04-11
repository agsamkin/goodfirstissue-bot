package agsamkin.code.service.impl;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;
import agsamkin.code.service.GitHubService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.RepoService;
import agsamkin.code.service.ScheduleService;
import agsamkin.code.util.GitHubUtil;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final LanguageService languageService;
    private final GitHubService gitHubService;
    private final RepoService repoService;
    private final GitHubUtil gitHubUtil;

    @SneakyThrows
    @Override
    public void uploadRepos() {
        Set<Language> languages = languageService.getAllLanguages();

        for (Language language : languages) {
            List<GHRepository> ghRepos = gitHubService.getReposByLanguage(language);

            for (GHRepository ghRepo : ghRepos) {
                if (!gitHubUtil.isActiveRepo(ghRepo)) {
                    continue;
                }

                if (!gitHubUtil.isMainLanguageInRepo(language, ghRepo)) {
                    continue;
                }

                List<GHIssue> ghIssues = gitHubService.getIssuesByRepo(ghRepo);
                if (ghIssues.isEmpty()) {
                    continue;
                }

                Repo newRepo = Repo.builder()
                        .repoId(ghRepo.getId())
                        .isPublic(GHRepository.Visibility.PUBLIC.equals(ghRepo.getVisibility()))
                        .isArchived(ghRepo.isArchived())
                        .isTemplate(ghRepo.isTemplate())
                        .isDisabled(ghRepo.isDisabled())
                        .name(ghRepo.getName())
                        .fullName(ghRepo.getFullName())
                        .description(ghRepo.getDescription())
                        .htmlUrl(ghRepo.getHtmlUrl().toString())
                        .url(ghRepo.getUrl().toString())
                        .forksCount(ghRepo.getForksCount())
                        .stargazersCount(ghRepo.getStargazersCount())
                        .watchersCount(ghRepo.getWatchersCount())
                        .createdAt(ghRepo.getCreatedAt())
                        .updatedAt(ghRepo.getUpdatedAt())
                        .pushedAt(ghRepo.getPushedAt())
                        .language(language).build();

                Map<Long, Issue> newIssues = new HashMap<>();

                for (GHIssue ghIssue : ghIssues) {
                    if (ghIssue.isLocked() || ghIssue.isPullRequest()) {
                        continue;
                    }

                    Issue newIssue = Issue.builder()
                            .issueId(ghIssue.getId())
                            .number(ghIssue.getNumber())
                            .title(ghIssue.getTitle())
                            .htmlUrl(ghIssue.getHtmlUrl().toString())
                            .url(ghIssue.getUrl().toString())
                            .isLocked(ghIssue.isLocked())
                            .commentsCount(ghIssue.getCommentsCount())
                            .createdAt(ghIssue.getCreatedAt())
                            .updatedAt(ghIssue.getUpdatedAt())
                            .closedAt(ghIssue.getClosedAt())
                            .build();

                    newIssues.put(ghIssue.getId(), newIssue);
                }

                if (newIssues.size() == 0) {
                    continue;
                }

                newRepo.setIssues(newIssues);
                repoService.updateRepo(newRepo);
            }

        }

    }

    @SneakyThrows
    @Override
    public void updateRepos() {
        Set<Language> languages = languageService.getAllLanguages();

        for (Language language : languages) {
            List<Repo> existingRepos = repoService.getReposToUpdateByLanguage(language);

            for (Repo existingRepo : existingRepos) {
                GHRepository ghRepo = gitHubService.getRepoByRepoId(existingRepo.getRepoId());
                if (!gitHubUtil.isActiveRepo(ghRepo)) {
                    repoService.deleteRepo(existingRepo);
                    continue;
                }

                if (!gitHubUtil.isMainLanguageInRepo(language, ghRepo)) {
                    repoService.deleteRepo(existingRepo);
                    continue;
                }

                existingRepo.setIsPublic(GHRepository.Visibility.PUBLIC.equals(ghRepo.getVisibility()));
                existingRepo.setIsArchived(ghRepo.isArchived());
                existingRepo.setIsTemplate(ghRepo.isTemplate());
                existingRepo.setIsDisabled(ghRepo.isDisabled());
                existingRepo.setName(ghRepo.getName());
                existingRepo.setFullName(ghRepo.getFullName());
                existingRepo.setDescription(ghRepo.getDescription());
                existingRepo.setHtmlUrl(ghRepo.getHtmlUrl().toString());
                existingRepo.setUrl(ghRepo.getUrl().toString());
                existingRepo.setForksCount(ghRepo.getForksCount());
                existingRepo.setStargazersCount(ghRepo.getStargazersCount());
                existingRepo.setWatchersCount(ghRepo.getWatchersCount());
                existingRepo.setCreatedAt(ghRepo.getCreatedAt());
                existingRepo.setUpdatedAt(ghRepo.getUpdatedAt());
                existingRepo.setPushedAt(ghRepo.getPushedAt());
                existingRepo.setLanguage(language);

                Map<Long, Issue> existingIssues = existingRepo.getIssues();
                Map<Long, Issue> newIssues = new HashMap<>();

                for (Map.Entry<Long, Issue> entry : existingIssues.entrySet()) {
                    Issue existingIssue = entry.getValue();
                    if (!Objects.isNull(existingIssue.getClosedAt())) {
                        continue;
                    }

                    GHIssue ghIssue = gitHubService.getIssueFromRepoByNumber(ghRepo, existingIssue.getNumber());
                    if (Objects.isNull(ghIssue)) {
                        continue;
                    }

                    if (ghIssue.isLocked() || !Objects.isNull(ghIssue.getClosedAt())) {
                        continue;
                    }

                    existingIssue.setTitle(ghIssue.getTitle());
                    existingIssue.setHtmlUrl(ghIssue.getHtmlUrl().toString());
                    existingIssue.setUrl(ghIssue.getUrl().toString());
                    existingIssue.setIsLocked(ghIssue.isLocked());
                    existingIssue.setCommentsCount(ghIssue.getCommentsCount());
                    existingIssue.setCreatedAt(ghIssue.getCreatedAt());
                    existingIssue.setUpdatedAt(ghIssue.getUpdatedAt());
                    existingIssue.setClosedAt(ghIssue.getClosedAt());

                    newIssues.put(existingIssue.getIssueId(), existingIssue);
                }

                existingRepo.setIssues(newIssues);
                repoService.saveRepo(existingRepo);
           }

        }

    }

    @SneakyThrows
    @Override
    public void deleteRepos() {
        Set<Language> languages = languageService.getAllLanguages();

        for (Language language : languages) {
            List<Repo> existingRepos = repoService.getReposToDeleteByLanguage(language);
            for (Repo existingRepo : existingRepos) {
                repoService.deleteRepo(existingRepo);
            }
        }

    }


}
