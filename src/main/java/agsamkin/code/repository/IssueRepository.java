package agsamkin.code.repository;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
//    List<Issue> findAllByRepoAndIsLockedAndClosedAt(
//            Repo repo, Boolean isLocked, Date closedAt);
}
