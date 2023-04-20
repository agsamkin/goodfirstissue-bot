package agsamkin.code.service.impl;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import agsamkin.code.repository.IssueRepository;
import agsamkin.code.service.IssueService;
import agsamkin.code.util.GitHubUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static agsamkin.code.service.GitHubService.MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT;
import static agsamkin.code.service.SendMessageService.ISSUES_SORT_PROPERTY;

@Transactional
@RequiredArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {
    private final IssueRepository issueRepository;
    private final GitHubUtil gitHubUtil;

    @Transactional(readOnly = true)
    @Override
    public List<Issue> getAllIssuesByRepo(Repo repo) {
//        return issueRepository.findAllByRepoAndUpdatedAtGreaterThanEqual(repo
//                , gitHubUtil.getLastDateUpdatedIssueFilter()
//                , PageRequest.of(0, MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT
//                        , Sort.by(Sort.Direction.DESC, ISSUES_SORT_PROPERTY)));
        return issueRepository.findAllByRepoAndUpdatedAtGreaterThanEqual(repo
                , gitHubUtil.getLastDateUpdatedIssueFilter()
                , PageRequest.of(0, MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT
                        , Sort.by(Sort.Direction.DESC, "repo_stargazersCount")));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Issue> getAllIssuesByRepos(List<Repo> repos, PageRequest of) {
        return issueRepository.findAllByRepoIn(repos, of);

    }

}
