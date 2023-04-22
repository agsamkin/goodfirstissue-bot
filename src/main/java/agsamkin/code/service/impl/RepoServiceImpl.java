package agsamkin.code.service.impl;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;

import agsamkin.code.repository.RepoRepository;
import agsamkin.code.service.RepoService;

import agsamkin.code.util.GitHubUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static agsamkin.code.util.GitHubUtil.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;

@Transactional
@RequiredArgsConstructor
@Service
public class RepoServiceImpl implements RepoService {
    private final RepoRepository repoRepository;
    private final GitHubUtil gitHubUtil;

    @Transactional(readOnly = true)
    @Override
    public List<Repo> getReposToUpdateByLanguage(Language language) {
        return repoRepository.findAllReposToUpdateByLanguage(
                language.getId(), gitHubUtil.getLastDateUpdatedRepoFilter()
                , MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Repo> getReposToDeleteByLanguage(Language language) {
        return repoRepository.findAllReposToDeleteByLanguage(
                language.getId(), gitHubUtil.getReposDeletionDate());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Repo> getAllReposByLanguages(List<Language> languages, PageRequest of) {
        List<Repo> repos = repoRepository.findAllByLanguageInAndUpdatedAtGreaterThanEqual(
                languages, gitHubUtil.getReposDeletionDate(), of);

        int reposCountPrevPage = of.getPageNumber() * of.getPageSize() ;
        int reposCountCurrentPage = reposCountPrevPage + repos.size();
        if (reposCountCurrentPage > MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT) {
            List<Repo> limitedRepos = new ArrayList<>();
            for (Repo repo : repos) {
                if ((reposCountPrevPage + limitedRepos.size()) >= MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT) {
                    break;
                }
                limitedRepos.add(repo);
            }
            return limitedRepos;
        }
        return repos;
    }

    @Override
    public Repo saveRepo(Repo repo) {
        return repoRepository.save(repo);
    }

    @Override
    public Repo updateRepo(Repo repo) {
        return repoRepository.save(
                repoRepository.findByRepoId(repo.getRepoId())
                        .map(existingRepo -> {
                            existingRepo.setIsPublic((repo.getIsPublic()));
                            existingRepo.setIsArchived(repo.getIsArchived());
                            existingRepo.setIsTemplate(repo.getIsTemplate());
                            existingRepo.setIsDisabled(repo.getIsDisabled());
                            existingRepo.setName(repo.getName());
                            existingRepo.setFullName(repo.getFullName());
                            existingRepo.setDescription(repo.getDescription());
                            existingRepo.setHtmlUrl(repo.getHtmlUrl());
                            existingRepo.setUrl(repo.getUrl());
                            existingRepo.setForksCount(repo.getForksCount());
                            existingRepo.setStargazersCount(repo.getStargazersCount());
                            existingRepo.setWatchersCount(repo.getWatchersCount());
                            existingRepo.setCreatedAt(repo.getCreatedAt());
                            existingRepo.setUpdatedAt(repo.getUpdatedAt());
                            existingRepo.setPushedAt(repo.getPushedAt());
                            existingRepo.setLanguage(repo.getLanguage());

                            Map<Long, Issue> issues = repo.getIssues();
                            Map<Long, Issue> existingIssues = existingRepo.getIssues();

                            for (Map.Entry<Long, Issue> entry : issues.entrySet()) {
                                Issue issue = entry.getValue();
                                if (existingIssues.containsKey(issue.getIssueId())) {
                                    Issue existingIssue = existingIssues.get(issue.getIssueId());
                                    existingIssue.setNumber(issue.getNumber());
                                    existingIssue.setTitle(issue.getTitle());
                                    existingIssue.setHtmlUrl(issue.getHtmlUrl());
                                    existingIssue.setUrl(issue.getUrl());
                                    existingIssue.setIsLocked(issue.getIsLocked());
                                    existingIssue.setCommentsCount(issue.getCommentsCount());
                                    existingIssue.setCreatedAt(issue.getCreatedAt());
                                    existingIssue.setUpdatedAt(issue.getUpdatedAt());
                                    existingIssue.setClosedAt(issue.getClosedAt());
                                } else {
                                    Issue newIssue = Issue.builder().issueId(issue.getIssueId())
                                            .number(issue.getNumber())
                                            .title(issue.getTitle())
                                            .htmlUrl(issue.getHtmlUrl())
                                            .url(issue.getUrl())
                                            .isLocked(issue.getIsLocked())
                                            .commentsCount(issue.getCommentsCount())
                                            .createdAt(issue.getCreatedAt())
                                            .updatedAt(issue.getUpdatedAt())
                                            .closedAt(issue.getClosedAt()).build();
                                    existingIssues.put(issue.getIssueId(), newIssue);
                                }
                            }
                            return existingRepo;
                        })
                        .orElse(repo)
        );
    }

    @Override
    public void deleteRepo(Repo repo) {
        repoRepository.delete(repo);
    }
}
