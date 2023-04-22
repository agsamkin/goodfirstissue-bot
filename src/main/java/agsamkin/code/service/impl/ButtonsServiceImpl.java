package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.model.setting.IssueOrder;
import agsamkin.code.model.setting.IssueSort;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.model.setting.Setting;

import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.UserService;
import agsamkin.code.util.ButtonsUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static agsamkin.code.util.ButtonsUtil.ACTION_SEPARATOR;
import static agsamkin.code.util.ButtonsUtil.ISSUE_NEXT_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_ORDER_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_ORDER_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_ORDER_TEXT;
import static agsamkin.code.util.ButtonsUtil.ISSUE_PREV_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_SORT_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_SORT_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_SORT_TEXT;
import static agsamkin.code.util.ButtonsUtil.LANGUAGES_PER_ROW;
import static agsamkin.code.util.ButtonsUtil.MARK_EMOJI;
import static agsamkin.code.util.ButtonsUtil.NEXT_PAGE_TEXT_TEMPLATE;
import static agsamkin.code.util.ButtonsUtil.PREV_PAGE_TEXT_TEMPLATE;
import static agsamkin.code.util.ButtonsUtil.REMOVE_LANGUAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_NEXT_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_ORDER_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_ORDER_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_ORDER_TEXT;
import static agsamkin.code.util.ButtonsUtil.REPO_PREV_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_SORT_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_SORT_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_SORT_TEXT;
import static agsamkin.code.util.ButtonsUtil.SETTING_ACTION;
import static agsamkin.code.util.ButtonsUtil.SETTING_BACK_ACTION;
import static agsamkin.code.util.ButtonsUtil.SETTING_BACK_TEXT;
import static agsamkin.code.util.ButtonsUtil.SET_LANGUAGE_ACTION;

@RequiredArgsConstructor
@Service
public class ButtonsServiceImpl implements ButtonsService {
    private final LanguageService languageService;
    private final UserService userService;
    private final ButtonsUtil buttonsUtil;

    @Override
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
                text = MARK_EMOJI + text;
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

    @Override
    public InlineKeyboardMarkup getReposButtons(int page, int size, boolean isLastPage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        if (page > 0) {
            int prevPage = buttonsUtil.getPreviousPage(page);
            int prevFrom = buttonsUtil.getPreviousSizePageFrom(page, size);
            int prevTo = buttonsUtil.getPreviousSizePageTo(page, size);

            String prevText = PREV_PAGE_TEXT_TEMPLATE.formatted(prevFrom, prevTo);
            String prevCallbackData = REPO_PREV_PAGE_ACTION + ACTION_SEPARATOR + prevPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(prevText)
                    .callbackData(prevCallbackData).build());
        }

        if (!isLastPage) {
            int nextPage = buttonsUtil.getNextPage(page);
            int nextFrom = buttonsUtil.getNextSizePageFrom(page, size);
            int nextTo = buttonsUtil.getNextSizePageTo(page, size);

            String nextText = NEXT_PAGE_TEXT_TEMPLATE.formatted(nextFrom, nextTo);
            String nextCallbackData = REPO_NEXT_PAGE_ACTION + ACTION_SEPARATOR + nextPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(nextText)
                    .callbackData(nextCallbackData).build());

        }

        buttons.add(buttonsRow);
        return new InlineKeyboardMarkup(buttons);
    }

    @Override
    public InlineKeyboardMarkup getIssuesButtons(int page, int size, boolean isLastPage) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        if (page > 0) {
            int prevPage = buttonsUtil.getPreviousPage(page);
            int prevFrom = buttonsUtil.getPreviousSizePageFrom(page, size);
            int prevTo = buttonsUtil.getPreviousSizePageTo(page, size);

            String prevText = PREV_PAGE_TEXT_TEMPLATE.formatted(prevFrom, prevTo);
            String prevCallbackData = ISSUE_PREV_PAGE_ACTION + ACTION_SEPARATOR + prevPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(prevText)
                    .callbackData(prevCallbackData).build());
        }

        if (!isLastPage) {
            int nextPage = buttonsUtil.getNextPage(page);
            int nextFrom = buttonsUtil.getNextSizePageFrom(page, size);
            int nextTo = buttonsUtil.getNextSizePageTo(page, size);

            String nextText = NEXT_PAGE_TEXT_TEMPLATE.formatted(nextFrom, nextTo);
            String nextCallbackData = ISSUE_NEXT_PAGE_ACTION + ACTION_SEPARATOR + nextPage;

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(nextText)
                    .callbackData(nextCallbackData).build());

        }

        buttons.add(buttonsRow);
        return new InlineKeyboardMarkup(buttons);
    }

    @Override
    public InlineKeyboardMarkup getSettingsButtons() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> buttonsRowRepo = new ArrayList<>();

        buttonsRowRepo.add(InlineKeyboardButton.builder()
                .text(REPO_SORT_TEXT)
                .callbackData(REPO_SORT_ACTION).build());

        buttonsRowRepo.add(InlineKeyboardButton.builder()
                .text(REPO_ORDER_TEXT)
                .callbackData(REPO_ORDER_ACTION).build());

        buttons.add(buttonsRowRepo);

        List<InlineKeyboardButton> buttonsRowIssue = new ArrayList<>();

        buttonsRowIssue.add(InlineKeyboardButton.builder()
                .text(ISSUE_SORT_TEXT)
                .callbackData(ISSUE_SORT_ACTION).build());

        buttonsRowIssue.add(InlineKeyboardButton.builder()
                .text(ISSUE_ORDER_TEXT)
                .callbackData(ISSUE_ORDER_ACTION).build());

        buttons.add(buttonsRowIssue);

        return new InlineKeyboardMarkup(buttons);
    }

    @Override
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
                text = MARK_EMOJI + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(REPO_SORT_OPTIONS_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    @Override
    public InlineKeyboardMarkup getRepoSortOrderButtons(Setting setting) {
        RepoOrder repoOrder = setting.getRepoOrder();
        if (Objects.isNull(repoOrder)) {
            repoOrder = RepoOrder.DESC;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (RepoOrder value : RepoOrder.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (repoOrder.equals(value)) {
                text = MARK_EMOJI + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(REPO_ORDER_OPTIONS_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    @Override
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
                text = MARK_EMOJI + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(ISSUE_SORT_OPTIONS_ACTION + ACTION_SEPARATOR + value).build());
            buttons.add(buttonsRow);
        }

        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
        buttonsRow.add(InlineKeyboardButton.builder()
                .text(SETTING_BACK_TEXT)
                .callbackData(SETTING_BACK_ACTION + ACTION_SEPARATOR + SETTING_ACTION).build());
        buttons.add(buttonsRow);

        return new InlineKeyboardMarkup(buttons);
    }

    @Override
    public InlineKeyboardMarkup getIssueSortOrderButtons(Setting setting) {
        IssueOrder issueOrder = setting.getIssueOrder();
        if (Objects.isNull(issueOrder)) {
            issueOrder = IssueOrder.DESC;
        }

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (IssueOrder value : IssueOrder.values()) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

            String text = value.getName();
            if (issueOrder.equals(value)) {
                text = MARK_EMOJI + text;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(ISSUE_ORDER_OPTIONS_ACTION + ACTION_SEPARATOR + value).build());
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
