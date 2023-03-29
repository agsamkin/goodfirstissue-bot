package agsamkin.code.service;

import agsamkin.code.model.Language;

import java.util.List;

public interface LanguageService {
    Language getLanguageByName(String name);
    List<Language> getLanguagesByEnable(boolean enable);
    List<Language> updateLanguages();
}
