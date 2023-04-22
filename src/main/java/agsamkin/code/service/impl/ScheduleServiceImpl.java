package agsamkin.code.service.impl;

import agsamkin.code.exception.GHIssueGettingException;
import agsamkin.code.exception.GHRateLimitException;
import agsamkin.code.exception.GHRepoGettingException;
import agsamkin.code.exception.GHRepoLanguagesGettingException;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;

import agsamkin.code.model.job.Job;
import agsamkin.code.service.GitHubService;
import agsamkin.code.service.IssueService;
import agsamkin.code.service.JobService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.RepoService;
import agsamkin.code.service.ScheduleService;
import agsamkin.code.util.GitHubUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static agsamkin.code.model.job.JobType.UPDATE_REPOS;
import static agsamkin.code.model.job.JobType.UPLOAD_REPOS;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final LanguageService languageService;
    private final GitHubService gitHubService;
    private final GitHubUtil gitHubUtil;
    private final RepoService repoService;
    private final JobService jobService;

    private final IssueService issueService;

    @Async
    @Override
    public void uploadRepos() {
        Set<Language> languages = languageService.getAllLanguages();
        if (languages.isEmpty()) {
            return;
        }

        boolean receivedRateLimitException = false;
        for (Language language : languages) {
            if (receivedRateLimitException) {
                break;
            }

            Optional<Job> optJob = jobService.getJobByJobTypeAndLanguage(UPLOAD_REPOS, language);
            if (optJob.isPresent() && Objects.nonNull(optJob.get().getCompletedAt())) {
                continue;
            }

            log.info(language.getName());

            List<GHRepository> ghRepos;
            try {
                ghRepos = gitHubService.getReposByLanguage(language);
            } catch (GHRateLimitException e) {
                receivedRateLimitException = true;
                log.info(e.getMessage());
                break;
            }

            for (GHRepository ghRepo : ghRepos) {
                if (receivedRateLimitException) {
                    break;
                }

                if (!gitHubUtil.isActiveRepo(ghRepo)) {
                    continue;
                }

                Map<String, Long> repoLanguages;
                try {
                    repoLanguages = gitHubService.getRepoLanguages(ghRepo);
                } catch (GHRateLimitException e) {
                    receivedRateLimitException = true;
                    log.info(e.getMessage());
                    break;
                } catch (GHRepoLanguagesGettingException e) {
                    log.error(e.getMessage());
                    continue;
                }

                if (!gitHubUtil.isMainLanguageInRepo(repoLanguages, language)) {
                    continue;
                }

                List<GHIssue> ghIssues;
                try {
                    ghIssues = gitHubService.getIssuesByRepo(ghRepo);
                } catch (GHRateLimitException e) {
                    receivedRateLimitException = true;
                    log.info(e.getMessage());
                    break;
                }

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
                        .pushedAt(ghRepo.getPushedAt())
                        .language(language).build();

                try {
                    newRepo.setCreatedAt(ghRepo.getCreatedAt());
                    newRepo.setUpdatedAt(ghRepo.getUpdatedAt());
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }

                Map<Long, Issue> newIssues = new HashMap<>();

                for (GHIssue ghIssue : ghIssues) {
                    if (receivedRateLimitException) {
                        break;
                    }

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
                            .closedAt(ghIssue.getClosedAt())
                            .build();

                    try {
                        newIssue.setCreatedAt(ghIssue.getCreatedAt());
                        newIssue.setUpdatedAt(ghIssue.getUpdatedAt());
                    } catch (IOException e) {
                        log.error(String.valueOf(e));
                    }

                    newIssues.put(ghIssue.getId(), newIssue);
                }

                if (newIssues.size() == 0) {
                    continue;
                }

                newRepo.setIssues(newIssues);
                repoService.updateRepo(newRepo);
            }

            if (!receivedRateLimitException) {
                jobService.updateJob(Job.builder()
                        .jobType(UPLOAD_REPOS)
                        .language(language)
                        .completedAt(new Date()).build());
            }
        }

        if (!receivedRateLimitException) {
            jobService.resetAllJobsByScheduleType(UPLOAD_REPOS);
        }
    }

    @Async
    @Override
    public void updateRepos() {
        Set<Language> languages = languageService.getAllLanguages();
        if (languages.isEmpty()) {
            return;
        }

        boolean receivedRateLimitException = false;
        for (Language language : languages) {
            if (receivedRateLimitException) {
                break;
            }

            Optional<Job> optJob = jobService.getJobByJobTypeAndLanguage(UPDATE_REPOS, language);
            if (optJob.isPresent() && Objects.nonNull(optJob.get().getCompletedAt())) {
                continue;
            }

            log.info(language.getName());

            List<Repo> existingRepos = repoService.getReposToUpdateByLanguage(language);

            for (Repo existingRepo : existingRepos) {
                if (receivedRateLimitException) {
                    break;
                }

                GHRepository ghRepo;
                try {
                    ghRepo = gitHubService.getRepoByRepoId(existingRepo.getRepoId());
                } catch (GHRateLimitException e) {
                    receivedRateLimitException = true;
                    log.info(e.getMessage());
                    break;
                } catch (GHRepoGettingException e) {
                    log.error(e.getMessage());
                    if (gitHubUtil.hasRepoNotFound(e)) {
                        repoService.deleteRepo(existingRepo);
                    }
                    continue;
                }

                if (!gitHubUtil.isActiveRepo(ghRepo)) {
                    repoService.deleteRepo(existingRepo);
                    continue;
                }

                Map<String, Long> repoLanguages;
                try {
                    repoLanguages = gitHubService.getRepoLanguages(ghRepo);
                } catch (GHRateLimitException e) {
                    receivedRateLimitException = true;
                    log.info(e.getMessage());
                    break;
                } catch (GHRepoLanguagesGettingException e) {
                    log.error(e.getMessage());
                    continue;
                }

                if (!gitHubUtil.isMainLanguageInRepo(repoLanguages, language)) {
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
                existingRepo.setPushedAt(ghRepo.getPushedAt());
                existingRepo.setLanguage(language);

                try {
                    existingRepo.setCreatedAt(ghRepo.getCreatedAt());
                    existingRepo.setUpdatedAt(ghRepo.getUpdatedAt());
                } catch (IOException e) {
                    log.error(String.valueOf(e));;
                }

                List<Issue> existingIssues = issueService.getAllIssuesByRepo(existingRepo);
                Map<Long, Issue> newIssues = new HashMap<>();

                for (Issue existingIssue : existingIssues) {
                    if (receivedRateLimitException) {
                        break;
                    }

                    if (!Objects.isNull(existingIssue.getClosedAt())) {
                        continue;
                    }

                    GHIssue ghIssue;
                    try {
                        ghIssue = gitHubService.getIssueFromRepoByNumber(ghRepo, existingIssue.getNumber());
                    } catch (GHRateLimitException e) {
                        receivedRateLimitException = true;
                        log.info(e.getMessage());
                        break;
                    } catch (GHIssueGettingException e) {
                        log.error(e.getMessage());
                        if (!gitHubUtil.hasIssueDeleted(e)) {
                            existingRepo.setIssues(newIssues);
                        }
                        continue;
                    }

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
                    existingIssue.setClosedAt(ghIssue.getClosedAt());

                    try {
                        existingIssue.setCreatedAt(ghIssue.getCreatedAt());
                        existingIssue.setUpdatedAt(ghIssue.getUpdatedAt());
                    } catch (IOException e) {
                        log.error(String.valueOf(e));;
                    }

                    newIssues.put(existingIssue.getIssueId(), existingIssue);
                }

                if (!receivedRateLimitException) {
                    existingRepo.setIssues(newIssues);
                    repoService.saveRepo(existingRepo);
                }
           }

            if (!receivedRateLimitException) {
                jobService.updateJob(Job.builder()
                        .jobType(UPDATE_REPOS)
                        .language(language)
                        .completedAt(new Date()).build());
           }
        }

        if (!receivedRateLimitException) {
            jobService.resetAllJobsByScheduleType(UPDATE_REPOS);
        }
    }

    @Async
    @Override
    public void deleteRepos() {
        Set<Language> languages = languageService.getAllLanguages();
        if (languages.isEmpty()) {
            return;
        }

        for (Language language : languages) {
            List<Repo> existingRepos = repoService.getReposToDeleteByLanguage(language);
            for (Repo existingRepo : existingRepos) {
                repoService.deleteRepo(existingRepo);
            }
        }
    }
}
