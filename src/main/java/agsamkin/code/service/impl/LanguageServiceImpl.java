package agsamkin.code.service.impl;

import agsamkin.code.api.github.GitHubApi;
import agsamkin.code.exception.LanguageNotFoundException;
import agsamkin.code.exception.LanguagesParsingException;
import agsamkin.code.model.Language;
import agsamkin.code.repository.LanguageRepository;
import agsamkin.code.service.LanguageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final GitHubApi gitHubApi;

    @Transactional(readOnly = true)
    @Override
    public Language getLanguageByName(String name) {
        return languageRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new LanguageNotFoundException("Language not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Language> getLanguagesByShowInMenu(boolean showInMenu) {
        return languageRepository.findByShowInMenu(showInMenu);
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
            Language existingLanguage =
                    languageRepository.findByNameIgnoreCase(language.getName())
                            .map(l -> {
                                l.setName(language.getName());
                                return l;
                            })
                            .orElse(
                                Language.builder()
                                            .name(language.getName())
                                            .showInMenu(false).build()
                            );
            languageRepository.save(existingLanguage);
        }

        return languages;
    }

}
