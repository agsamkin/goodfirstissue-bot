package agsamkin.code.repository;

import agsamkin.code.model.Language;
import agsamkin.code.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByNameIgnoreCase(String name);
    List<Language> findByEnable(boolean enable);
}
