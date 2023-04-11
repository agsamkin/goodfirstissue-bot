package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;

import java.util.List;

public interface RepoService {
   List<Repo> getReposToUpdateByLanguage(Language language);
   List<Repo> getReposToDeleteByLanguage(Language language);

   Repo saveRepo(Repo repo);
   Repo updateRepo(Repo repo);
   void deleteRepo(Repo repo);
}
