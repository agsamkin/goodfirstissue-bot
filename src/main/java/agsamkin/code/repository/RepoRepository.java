package agsamkin.code.repository;

import agsamkin.code.model.Language;
import agsamkin.code.model.repo.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoRepository extends JpaRepository<Repo, Long> {
    List<Repo> findAllByLanguage(Language language);
    Optional<Repo> findByRepoId(Long repoId);
}
