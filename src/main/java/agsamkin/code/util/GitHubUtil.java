package agsamkin.code.util;

import agsamkin.code.model.Language;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Component
public class GitHubUtil {
    public static final int NUMBER_OF_MONTHS_TO_UPDATE_REPO = 1;
    public static final int NUMBER_OF_MONTHS_TO_DELETE_REPO = 3;

    public static final int NUMBER_OF_MONTHS_TO_UPDATE_ISSUE = 3;

    public static final double MIN_PERCENT_LANGUAGE_USAGE_IN_REPO = 70;

    public Date getLastDateUpdatedRepoFilter() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_UPDATE_REPO);
    }

    public Date getReposDeletionDate() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_DELETE_REPO);
    }

    public Date getLastDateUpdatedIssueFilter() {
        return DateUtils.addMonths(new Date(), -NUMBER_OF_MONTHS_TO_UPDATE_ISSUE);
    }

    public boolean isMainLanguageInRepo(Language language, GHRepository ghRepo) {
        if (getPercentLanguageUsageInRepo(language, ghRepo)
                < MIN_PERCENT_LANGUAGE_USAGE_IN_REPO) {
            return false;
        }
        return true;
    }

    @SneakyThrows
    public double getPercentLanguageUsageInRepo(Language language, GHRepository ghRepo) {
        Map<String, Long> repoLanguages = ghRepo.listLanguages();
        if (!repoLanguages.containsKey(language.getName())) {
            return 0;
        }

        if (repoLanguages.size() == 1) {
            return 100;
        }

        Long bytesThatLanguage = repoLanguages.get(language.getName());
        Long totalBytes = repoLanguages.values().stream().reduce(0L, Long::sum);
        if (totalBytes == 0L) {
            return 0;
        }

        return bytesThatLanguage * 100 / totalBytes;
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

}
