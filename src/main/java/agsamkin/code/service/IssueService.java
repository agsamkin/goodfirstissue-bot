package agsamkin.code.service;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IssueService {
    List<Issue> getAllIssuesByRepo(Repo repo);
    List<Issue> getAllIssuesByRepos(List<Repo> repos, PageRequest of);
}
