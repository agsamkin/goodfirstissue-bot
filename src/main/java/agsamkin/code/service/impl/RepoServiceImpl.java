package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.model.repo.Repo;
import agsamkin.code.repository.RepoRepository;
import agsamkin.code.service.RepoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class RepoServiceImpl implements RepoService {
    private final RepoRepository repoRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Repo> getReposByLanguages(Language language) {
        return repoRepository.findAllByLanguage(language);
    }

    @Override
    public Repo updateRepo(Repo repo) {
        return repoRepository.save(repo);
    }

    @Override
    public List<Repo> updateRepos(List<Repo> repos) {
        return repoRepository.saveAll(repos);
    }
}
