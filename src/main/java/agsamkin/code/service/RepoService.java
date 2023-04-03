package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.repo.Repo;

import java.util.List;

public interface RepoService {
   List<Repo> getReposByLanguages(Language language);
   Repo updateRepo(Repo repo);
   List<Repo> updateRepos(List<Repo> repos);
}
