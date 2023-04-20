package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RepoService {
   List<Repo> getReposToUpdateByLanguage(Language language);
   List<Repo> getReposToDeleteByLanguage(Language language);

   List<Repo> getAllReposByLanguages(List<Language> languages, PageRequest of);

   Repo saveRepo(Repo repo);
   Repo updateRepo(Repo repo);
   void deleteRepo(Repo repo);
}
