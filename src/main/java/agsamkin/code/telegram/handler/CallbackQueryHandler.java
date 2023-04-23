package agsamkin.code.telegram.handler;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import agsamkin.code.model.User;
import agsamkin.code.model.setting.IssueOrder;
import agsamkin.code.model.setting.IssueSort;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.IssueService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.RepoService;
import agsamkin.code.service.UserService;
import agsamkin.code.telegram.TgBot;

import agsamkin.code.util.SendMessageUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static agsamkin.code.util.ButtonsUtil.ACTION_SEPARATOR;
import static agsamkin.code.util.ButtonsUtil.ISSUE_NEXT_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_ORDER_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_ORDER_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_PREV_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_SORT_ACTION;
import static agsamkin.code.util.ButtonsUtil.ISSUE_SORT_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.REMOVE_LANGUAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_NEXT_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_ORDER_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_ORDER_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_PREV_PAGE_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_SORT_ACTION;
import static agsamkin.code.util.ButtonsUtil.REPO_SORT_OPTIONS_ACTION;
import static agsamkin.code.util.ButtonsUtil.SETTING_ACTION;
import static agsamkin.code.util.ButtonsUtil.SETTING_BACK_ACTION;
import static agsamkin.code.util.ButtonsUtil.SET_LANGUAGE_ACTION;
import static agsamkin.code.util.GitHubUtil.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;
import static agsamkin.code.util.SendMessageUtil.ISSUES_PAGE_SIZE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.ISSUE_ORDER_COMMAND_TEXT;
import static agsamkin.code.util.SendMessageUtil.ISSUE_SORT_COMMAND_TEXT;
import static agsamkin.code.util.SendMessageUtil.REPOS_PAGE_SIZE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.REPO_ORDER_COMMAND_TEXT;
import static agsamkin.code.util.SendMessageUtil.REPO_SORT_COMMAND_TEXT;
import static agsamkin.code.util.SendMessageUtil.SETTINGS_MESSAGE;

@RequiredArgsConstructor
@Component
public class CallbackQueryHandler {
    private final LanguageService languageService;
    private final UserService userService;
    private final ButtonsService buttonsService;
    private final RepoService repoService;
    private final IssueService issueService;
    private final SendMessageUtil sendMessageUtil;
    private final TgBot tgBot;

    public void handleCallback(CallbackQuery callbackQuery) {
        long userId = callbackQuery.getFrom().getId();
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();

        String action = data;
        String value = data;

        if (data.contains(ACTION_SEPARATOR)) {
            String[] params = data.split(ACTION_SEPARATOR);
            action = params[0];
            value = params[1];
        }

        if (SET_LANGUAGE_ACTION.equals(action)) {
            userService.setLanguage(userId, languageService.getLanguageByName(value));

            EditMessageReplyMarkup editMessage =
                    sendMessageUtil.getEditMessageReplyMarkup(message,
                            buttonsService.getSetupMyLanguageButtons(userId));
            tgBot.sendMessage(editMessage);
        } else if (REMOVE_LANGUAGE_ACTION.equals(action)) {
            userService.removeLanguage(userId, languageService.getLanguageByName(value));

            EditMessageReplyMarkup editMessage =
                    sendMessageUtil.getEditMessageReplyMarkup(message,
                            buttonsService.getSetupMyLanguageButtons(userId));
            tgBot.sendMessage(editMessage);
        } else if (REPO_NEXT_PAGE_ACTION.equals(action)
                || REPO_PREV_PAGE_ACTION.equals(action)) {

            int page = Integer.parseInt(value);

            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();

            List<Repo> repos = repoService.getAllReposByLanguages(
                    userService.getUserLanguages(userId),
                    PageRequest.of(page, REPOS_PAGE_SIZE_DEFAULT,
                            Sort.by(setting.getRepoOrder().getDirection(),
                                    setting.getRepoSort().getSortProperty()
                            )
                    )
            );

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, sendMessageUtil.getMarkDownTextForReposMessage(repos),
                    buttonsService.getReposButtons(page, REPOS_PAGE_SIZE_DEFAULT,
                            repos.size() < REPOS_PAGE_SIZE_DEFAULT));
            tgBot.sendMessage(editMessage);
        } else if (REPO_SORT_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, REPO_SORT_COMMAND_TEXT,
                    buttonsService.getRepoSortButtons(user.getSetting()));
            tgBot.sendMessage(editMessage);
        } else if (REPO_SORT_OPTIONS_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setRepoSort(RepoSort.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, REPO_SORT_COMMAND_TEXT, buttonsService.getRepoSortButtons(setting));
            tgBot.sendMessage(editMessage);
        } else if (REPO_ORDER_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, REPO_ORDER_COMMAND_TEXT, buttonsService.getRepoSortOrderButtons(user.getSetting()));
            tgBot.sendMessage(editMessage);
        } else if (REPO_ORDER_OPTIONS_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setRepoOrder(RepoOrder.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, REPO_ORDER_COMMAND_TEXT, buttonsService.getRepoSortOrderButtons(setting));
            tgBot.sendMessage(editMessage);
        } else if (ISSUE_NEXT_PAGE_ACTION.equals(action)
                || ISSUE_PREV_PAGE_ACTION.equals(action)) {

            int page = Integer.parseInt(value);

            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();

            List<Repo> repos = repoService.getAllReposByLanguages(
                    userService.getUserLanguages(userId),
                    PageRequest.of(0, MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT)
            );

            List<Issue> issues = issueService.getAllIssuesByRepos(repos,
                    PageRequest.of(page, ISSUES_PAGE_SIZE_DEFAULT,
                            Sort.by(setting.getIssueOrder().getDirection(),
                                    setting.getIssueSort().getSortProperty()
                            )
                    )
            );

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, sendMessageUtil.getMarkDownTextForIssuesMessage(issues),
                    buttonsService.getIssuesButtons(page, ISSUES_PAGE_SIZE_DEFAULT,
                            issues.size() < ISSUES_PAGE_SIZE_DEFAULT));
            tgBot.sendMessage(editMessage);
        } else if (ISSUE_SORT_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, ISSUE_SORT_COMMAND_TEXT, buttonsService.getIssueSortButtons(user.getSetting()));
            tgBot.sendMessage(editMessage);
        } else if (ISSUE_SORT_OPTIONS_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setIssueSort(IssueSort.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, ISSUE_SORT_COMMAND_TEXT, buttonsService.getIssueSortButtons(setting));
            tgBot.sendMessage(editMessage);
        } else if (ISSUE_ORDER_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, ISSUE_ORDER_COMMAND_TEXT, buttonsService.getIssueSortOrderButtons(user.getSetting()));
            tgBot.sendMessage(editMessage);
        } else if (ISSUE_ORDER_OPTIONS_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setIssueOrder(IssueOrder.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                    message, ISSUE_ORDER_COMMAND_TEXT, buttonsService.getIssueSortOrderButtons(setting));
            tgBot.sendMessage(editMessage);
        } else if (SETTING_BACK_ACTION.equals(action)) {
            if (SETTING_ACTION.equals(value)) {
                EditMessageText editMessage = sendMessageUtil.getEditMessageText(
                        message, SETTINGS_MESSAGE, buttonsService.getSettingsButtons());

                tgBot.sendMessage(editMessage);
            }
        }
    }
}
