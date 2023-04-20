package agsamkin.code.repository;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findAllByRepoAndUpdatedAtGreaterThanEqual(Repo repo, Date updatedAt, PageRequest of);

    List<Issue> findAllByRepoIn(List<Repo> repos, PageRequest of);
}
