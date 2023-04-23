package agsamkin.code.service;

import agsamkin.code.model.Language;

import java.util.Set;

public interface LanguageService {
    Set<Language> updateLanguages();

    Set<Language> getAllLanguages();
    Language getLanguageByName(String value);
}
