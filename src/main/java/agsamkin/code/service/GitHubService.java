package agsamkin.code.service;

import agsamkin.code.exception.GHIssueGettingException;
import agsamkin.code.exception.GHLanguageGettingException;
import agsamkin.code.exception.GHRateLimitException;
import agsamkin.code.exception.GHRateLimitGettingException;
import agsamkin.code.exception.GHRepoGettingException;
import agsamkin.code.exception.GHRepoLanguagesGettingException;
import agsamkin.code.model.Language;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.RateLimitTarget;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GitHubService {
    Set<String> getAllLanguages() throws GHLanguageGettingException;

    Map<String, Long> getRepoLanguages(GHRepository ghRepo)
            throws GHRepoLanguagesGettingException, GHRateLimitException;

    GHRepository getRepoByRepoId(Long repoId)
            throws GHRepoGettingException, GHRateLimitException;
    List<GHRepository> getReposByLanguage(Language language, Date updatedFrom)
            throws GHRateLimitException;
    List<GHRepository> getReposByLanguage(Language language) throws GHRateLimitException;

    GHIssue getIssueFromRepoByNumber(GHRepository ghRepo, Integer number)
            throws GHIssueGettingException, GHRateLimitException;
    List<GHIssue> getIssuesByRepo(GHRepository ghRepo, Date since)
            throws GHRateLimitException;
    List<GHIssue> getIssuesByRepo(GHRepository ghRepo) throws GHRateLimitException;

    GHRateLimit getRateLimit() throws GHRateLimitGettingException;
    void checkRateLimit(RateLimitTarget rateLimitTarget) throws GHRateLimitException;
}
