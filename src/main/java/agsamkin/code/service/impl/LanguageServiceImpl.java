package agsamkin.code.service.impl;

import agsamkin.code.exception.LanguageNotFoundException;
import agsamkin.code.model.Language;
import agsamkin.code.repository.LanguageRepository;
import agsamkin.code.service.GitHubService;
import agsamkin.code.service.LanguageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.TreeSet;

@Transactional
@RequiredArgsConstructor
@Service
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final GitHubService gitHubService;

    @Override
    public Set<Language> updateLanguages() {
        Set<String> languagesFromGitHub = gitHubService.getAllLanguages();

        Set<Language> languages = new TreeSet<>();
        for (String language : languagesFromGitHub) {
            Language existingLanguage =
                    languageRepository.findByNameIgnoreCase(language)
                            .map(l -> {
                                l.setName(language);
                                return l;
                            })
                            .orElse(
                                    Language.builder()
                                            .name(language)
                                            .enable(false).build()
                            );
            languageRepository.save(existingLanguage);
            languages.add(existingLanguage);
        }

        return languages;
    }

    @Transactional(readOnly = true)
    @Override
    public Set<Language> getAllLanguages() {
        return languageRepository.findByEnable(true);
    }

    @Transactional(readOnly = true)
    @Override
    public Language getLanguageByName(String name) {
        return languageRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new LanguageNotFoundException("Language not found"));
    }
}
