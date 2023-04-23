package agsamkin.code.util;

import agsamkin.code.exception.GHIssueGettingException;
import agsamkin.code.exception.GHRateLimitNotFoundException;
import agsamkin.code.exception.GHRepoGettingException;

import agsamkin.code.model.Language;

import org.apache.commons.lang3.time.DateUtils;

import org.kohsuke.github.GHRateLimit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.HttpException;
import org.kohsuke.github.RateLimitTarget;

import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static kong.unirest.HttpStatus.GONE;

import static org.kohsuke.github.RateLimitTarget.CORE;
import static org.kohsuke.github.RateLimitTarget.GRAPHQL;
import static org.kohsuke.github.RateLimitTarget.INTEGRATION_MANIFEST;
import static org.kohsuke.github.RateLimitTarget.SEARCH;

@Component
public class GitHubUtil {
    public static final String GITHUB_LANGUAGES_URL = "https://github.com/search/advanced";

    public static final String QUERY_REPOS =
            "archived:false template:false mirror:false forks:>=%s good-first-issues:>=%s";

    public static final int MIN_STARS_REPO_FILTER = 5;
    public static final int MIN_FORKS_REPO_FILTER = 5;
    public static final int MIN_GOOD_FIRST_ISSUES_FILTER = 1;
    public static final int MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT = 100;

    public static final String GOOD_FIRST_ISSUE_LABEL_FILTER = "good first issue";
    public static final int MAX_NUMBER_OF_ISSUES_IN_QUERY_RESULT = 20;

    public static final int NUMBER_OF_MONTHS_TO_UPDATE_REPO = 1;
    public static final int NUMBER_OF_MONTHS_TO_DELETE_REPO = 2;

    public static final int NUMBER_OF_MONTHS_TO_UPDATE_ISSUE = 3;

    public static final double MIN_PERCENT_LANGUAGE_USAGE_IN_REPO = 70;
    public static final double ONE_HUNDRED_PERCENT = 100;

    public static final int MIN_REMAINING_NUMBER_OF_REQUEST = 1;

    public Date getLastDateUpdatedRepoFilter() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_UPDATE_REPO);
    }

    public Date getReposDeletionDate() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_DELETE_REPO);
    }

    public Date getLastDateUpdatedIssueFilter() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_UPDATE_ISSUE);
    }

    public boolean isMainLanguageInRepo(Map<String, Long> repoLanguages, Language language) {
        if (getPercentLanguageUsageInRepo(repoLanguages, language)
                < MIN_PERCENT_LANGUAGE_USAGE_IN_REPO) {
            return false;
        }
        return true;
    }

    public double getPercentLanguageUsageInRepo(Map<String, Long> repoLanguages, Language language) {
        if (!repoLanguages.containsKey(language.getName())) {
            return 0;
        }

        if (repoLanguages.size() == 1) {
            return ONE_HUNDRED_PERCENT;
        }

        Long bytesThatLanguage = repoLanguages.get(language.getName());
        Long totalBytes = repoLanguages.values().stream().reduce(0L, Long::sum);
        if (totalBytes == 0L) {
            return 0;
        }

        return bytesThatLanguage * ONE_HUNDRED_PERCENT / totalBytes;
    }

    public boolean isActiveRepo(GHRepository ghRepo) {
        if (Objects.isNull(ghRepo)) {
            return false;
        }

        if (ghRepo.getVisibility() != GHRepository.Visibility.PUBLIC
                || ghRepo.isPrivate()
                || ghRepo.isArchived()
                || ghRepo.isTemplate()
                || ghRepo.isDisabled()) {
            return false;
        }

        return true;
    }

    public boolean hasRepoNotFound(GHRepoGettingException e) {
        if (!Objects.isNull(e.getCause())
                && e.getCause() instanceof FileNotFoundException) {
            return true;
        }
        return false;
    }

    public boolean hasIssueDeleted(GHIssueGettingException e) {
        if (!Objects.isNull(e.getCause())
                && e.getCause() instanceof HttpException
                && ((HttpException) e.getCause()).getResponseCode() == GONE) {
            return true;
        }
        return false;
    }

    public boolean checkRateLimitByTarget(
            GHRateLimit ghRateLimit, RateLimitTarget rateLimitTarget) {

        getRateLimitByTarget(ghRateLimit, rateLimitTarget).getRemaining();
        int remaining = getRateLimitByTarget(ghRateLimit, rateLimitTarget).getRemaining();

        if (remaining <= MIN_REMAINING_NUMBER_OF_REQUEST) {
            return false;
        }

        return true;
    }

    private GHRateLimit.Record getRateLimitByTarget(
            GHRateLimit ghRateLimit, RateLimitTarget rateLimitTarget) throws GHRateLimitNotFoundException {

        GHRateLimit.Record ghRateLimitRecord;
        if (rateLimitTarget == CORE) {
            ghRateLimitRecord = ghRateLimit.getCore();
        } else if (rateLimitTarget == SEARCH) {
            ghRateLimitRecord = ghRateLimit.getSearch();
        } else if (rateLimitTarget == GRAPHQL) {
            ghRateLimitRecord = ghRateLimit.getGraphQL();
        } else if (rateLimitTarget == INTEGRATION_MANIFEST) {
            ghRateLimitRecord = ghRateLimit.getIntegrationManifest();
        } else {
            throw new GHRateLimitNotFoundException("Rate limit not found");
        }
        return ghRateLimitRecord;
    }
}
