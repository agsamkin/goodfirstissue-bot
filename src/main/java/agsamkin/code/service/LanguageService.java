package agsamkin.code.service;

import agsamkin.code.model.Language;

import java.util.List;
import java.util.Optional;

public interface LanguageService {
    Optional<Language> getLanguageByName(String name);
    List<Language> getLanguagesByEnable(boolean enable);
    List<Language> updateLanguages();
}
