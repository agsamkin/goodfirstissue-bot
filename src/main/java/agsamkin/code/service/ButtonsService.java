package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.setting.IssueOrder;
import agsamkin.code.model.setting.IssueSort;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.model.setting.Setting;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ButtonsService {
    public static final int LANGUAGES_PER_ROW = 3;

    public static final String ACTION_SEPARATOR = ":";

    public static final String SET_LANGUAGE_ACTION = "set_lang";
    public static final String REMOVE_LANGUAGE_ACTION = "remove_lang";
    public static final String MARK = "\uD83D\uDCCC";

    public static final String NEXT_PAGE_TEXT_TEMPLATE = "Next (%s - %s) >>";
    public static final String PREV_PAGE_TEXT_TEMPLATE = "<< Prev (%s - %s)";

    public static final String REPO_NEXT_PAGE_ACTION = "repo_next_page";
    public static final String REPO_PREV_PAGE_ACTION = "repo_prev_page";

    public static final String REPO_SORT_TEXT = "Sort repository";
    public static final String REPO_SORT_ACTION = "repo_sort";
    public static final String REPO_SORT_OPTION_ACTION = "repo_sort_opt";

    public static final String REPO_ORDER_TEXT = "Order repository";
    public static final String REPO_ORDER_ACTION = "repo_order";
    public static final String REPO_ORDER_OPTION_ACTION = "repo_order_opt";

    public static final String ISSUE_NEXT_PAGE_ACTION = "issue_next_page";
    public static final String ISSUE_PREV_PAGE_ACTION = "issue_prev_page";

    public static final String ISSUE_SORT_TEXT = "Sort issue";
    public static final String ISSUE_SORT_ACTION = "issue_sort";
    public static final String ISSUE_SORT_OPTION_ACTION = "issue_sort_opt";

    public static final String ISSUE_ORDER_TEXT = "Order issue";
    public static final String ISSUE_ORDER_ACTION = "issue_order";
    public static final String ISSUE_ORDER_OPTION_ACTION = "issue_order_opt";

    public static final String SETTING_TEXT = "Settings";
    public static final String SETTING_ACTION = "settings";

    public static final String SETTING_BACK_TEXT = "<< Back to settings";
    public static final String SETTING_BACK_ACTION = "back";

    private final LanguageService languageService;
    private final UserService userService;

    public InlineKeyboardMarkup getSetupMyLanguageButtons(Long userId) {
        Set<Language> allLanguages = languageService.getAllLanguages();

        List<Language> userLanguages = userService.getUserLanguages(userId);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        int count = 0;
        for (Language language : allLanguages) {
            if (count >= LANGUAGES_PER_ROW) {
                buttons.add(buttonsRow);
                buttonsRow = new ArrayList<>();
                count = 0;
            }

            String text = language.getName();
            String callbackData = ACTION_SEPARATOR + language.getName();

            if (userLanguages.size() > 0 && userLanguages.contains(language)) {
                text = MARK + text;
                callbackData = REMOVE_LANGUAGE_ACTION + callbackData;
            } else {
                callbackData = SET_LANGUAGE_ACTION + callbackData;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(callbackData).build());
            count++;

        }

        if (buttonsRow.size() > 0) {
            buttons.add(buttonsRow);
        }

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getReposButtons(int page, int size, boolean isLastPage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        if (page > 0) {
            int prevPage = page - 1;
            int prevFrom = prevPage * size + 1;
            int prevTo = (prevPage + 1) * size;

            String prevText = PREV_PAGE_TEXT_TEMPLATE.formatted(prevFrom, prevTo);
            String prevCallbackData = REPO_PREV_PAGE_ACTION + ACTION_SEPARATOR + prevPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(prevText)
                    .callbackData(prevCallbackData).build());
        }

        if (!isLastPage) {
            int nextPage = page + 1;
            int nextFrom = nextPage * size + 1;
            int nextTo = (nextPage + 1) * size;

            String nextText = NEXT_PAGE_TEXT_TEMPLATE.formatted(nextFrom, nextTo);
            String nextCallbackData = REPO_NEXT_PAGE_ACTION + ACTION_SEPARATOR + nextPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(nextText)
                    .callbackData(nextCallbackData).build());

        }

        buttons.add(buttonsRow);
        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getIssuesButtons(int page, int size, boolean isLastPage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        if (page > 0) {
            int prevPage = page - 1;
            int prevFrom = prevPage * size + 1;
            int prevTo = (prevPage + 1) * size;

            String prevText = PREV_PAGE_TEXT_TEMPLATE.formatted(prevFrom, prevTo);
            String prevCallbackData = ISSUE_PREV_PAGE_ACTION + ACTION_SEPARATOR + prevPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(prevText)
                    .callbackData(prevCallbackData).build());
        }

        if (!isLastPage) {
            int nextPage = page + 1;
            int nextFrom = nextPage * size + 1;
            int nextTo = (nextPage + 1) * size;

            String nextText = NEXT_PAGE_TEXT_TEMPLATE.formatted(nextFrom, nextTo);
            String nextCallbackData = ISSUE_NEXT_PAGE_ACTION + ACTION_SEPARATOR + nextPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(nextText)
                    .callbackData(nextCallbackData).build());

        }

        buttons.add(buttonsRow);
        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getSettingsButtons() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> buttonsRow1 = new ArrayList<>();

        buttonsRow1.add(InlineKeyboardButton.builder()
                .text(REPO_SORT_TEXT)
                .callbackData(REPO_SORT_ACTION).build());

        buttonsRow1.add(InlineKeyboardButton.builder()
                .text(REPO_ORDER_TEXT)
                .callbackData(REPO_ORDER_ACTION).build());

        buttons.add(buttonsRow1);

        List<InlineKeyboardButton> buttonsRow2 = new ArrayList<>();

        buttonsRow2.add(InlineKeyboardButton.builder()
                .text(ISSUE_SORT_TEXT)
                .callbackData(ISSUE_SORT_ACTION).build());

        buttonsRow2.add(InlineKeyboardButton.builder()
                .text(ISSUE_ORDER_TEXT)
                .callbackData(ISSUE_ORDER_ACTION).build());

        buttons.add(buttonsRow2);

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getRepoSortButtons(Setting setting) {
        RepoSort repoSort = setting.getRepoSort();
        if (Objects.isNull(repoSort)) {
            repoSort = RepoSort.UPDATED;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (RepoSort value : RepoSort.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (repoSort.equals(value)) {
                text = MARK + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(REPO_SORT_OPTION_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getRepoOrderButtons(Setting setting) {
        RepoOrder repoOrder = setting.getRepoOrder();
        if (Objects.isNull(repoOrder)) {
            repoOrder = RepoOrder.DESC;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (RepoOrder value : RepoOrder.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (repoOrder.equals(value)) {
                text = MARK + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(REPO_ORDER_OPTION_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getIssueSortButtons(Setting setting) {
        IssueSort issueSort = setting.getIssueSort();
        if (Objects.isNull(issueSort)) {
            issueSort = IssueSort.UPDATED;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (IssueSort value : IssueSort.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (issueSort.equals(value)) {
                text = MARK + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(ISSUE_SORT_OPTION_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    public InlineKeyboardMarkup getIssueOrderButtons(Setting setting) {
        IssueOrder issueOrder = setting.getIssueOrder();
        if (Objects.isNull(issueOrder)) {
            issueOrder = IssueOrder.DESC;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (IssueOrder value : IssueOrder.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (issueOrder.equals(value)) {
                text = MARK + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(ISSUE_ORDER_OPTION_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }
}
