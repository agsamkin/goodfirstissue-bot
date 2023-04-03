package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.repo.Repo;

import java.util.Date;
import java.util.List;

public interface GitHubService {
    List<Language> getAllLanguages();
    List<Repo> getReposWithGoodFirstIssuesByLanguage(Language language);
    List<Repo> getReposWithGoodFirstIssuesByLanguageFromDate(Language language, Date repoFrom, Date issueFrom);
}

