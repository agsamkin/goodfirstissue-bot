package agsamkin.code.service.impl;

import agsamkin.code.api.github.GitHubApi;
import agsamkin.code.exception.LanguagesParsingException;
import agsamkin.code.model.Language;
import agsamkin.code.repository.LanguageRepository;
import agsamkin.code.service.LanguageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    private final GitHubApi gitHubApi;

    @Transactional(readOnly = true)
    @Override
    public Optional<Language> getLanguageByName(String name) {
        return languageRepository.findByNameIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Language> getLanguagesByEnable(boolean enable) {
        return languageRepository.findByEnable(enable);
    }

    @Override
    public List<Language> updateLanguages() {
        List<Language> languages;
        try {
            languages = gitHubApi.parseAllLanguagesFromGitHub();
        } catch (Exception e) {
            throw new LanguagesParsingException(e.getMessage());
        }

        for (Language language : languages) {
            Language existingLanguage = getLanguageByName(language.getName()).orElse(null);
            if (Objects.nonNull(existingLanguage)) {
                existingLanguage.setName(language.getName());
                languageRepository.save(existingLanguage);
            } else {
                Language newLanguage = new Language();
                newLanguage.setName(language.getName());
                newLanguage.setEnable(false);
                languageRepository.save(newLanguage);
            }
        }

        return languages;
    }

}
