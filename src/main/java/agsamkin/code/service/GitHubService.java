package agsamkin.code.service;

import agsamkin.code.model.Language;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface GitHubService {
    Set<String> getAllLanguages();

    GHRepository getRepoByRepoId(Long repoId);
    List<GHRepository> getReposByLanguage(Language language);
    List<GHRepository> getReposByLanguage(Language language, Date updatedFrom);

    GHIssue getIssueFromRepoByNumber(GHRepository ghRepo, Integer number);
    List<GHIssue> getIssuesByRepo(GHRepository ghRepo);
    List<GHIssue> getIssuesByRepo(GHRepository ghRepo, Date since);
}

