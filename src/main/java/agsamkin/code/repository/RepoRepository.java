package agsamkin.code.repository;

import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RepoRepository extends JpaRepository<Repo, Long> {
    Optional<Repo> findByRepoId(Long repoId);

    @Query(value = "SELECT * FROM repos r "
            + "WHERE r.language_id = :languageId "
            + "AND r.is_public AND NOT r.is_archived "
            + "AND NOT r.is_template AND NOT r.is_disabled "
            + "AND r.updated_at >= :updatedAt "
            + "ORDER BY updated_at DESC LIMIT :limit", nativeQuery = true)
    List<Repo> findAllReposToUpdateByLanguage(
            @Param("languageId") Long languageId,
            @Param("updatedAt") Date updatedAt,
            @Param("limit") Integer limit);

    @Query(value = "SELECT * FROM repos r "
            + "WHERE r.language_id = :languageId "
            + "AND (NOT r.is_public OR r.is_archived "
            + "OR r.is_template OR r.is_disabled "
            + "OR r.updated_at < :updated_at)"
            + "ORDER BY updated_at", nativeQuery = true)
    List<Repo> findAllReposToDeleteByLanguage(
            @Param("languageId") Long languageId,
            @Param("updated_at") Date updatedAt);

    List<Repo> findAllByLanguageInAndUpdatedAtGreaterThanEqual(
            List<Language> languages, Date reposDeletionDate, PageRequest of);
}
