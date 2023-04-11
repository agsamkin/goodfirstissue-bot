package agsamkin.code.repository;

import agsamkin.code.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByNameIgnoreCase(String name);
    Set<Language> findByEnable(Boolean enable);
}
