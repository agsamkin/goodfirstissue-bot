package agsamkin.code.telegram.handler;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
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
import lombok.SneakyThrows;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

import static agsamkin.code.service.ButtonsService.ACTION_SEPARATOR;
import static agsamkin.code.service.ButtonsService.ISSUE_NEXT_PAGE_ACTION;
import static agsamkin.code.service.ButtonsService.ISSUE_ORDER_ACTION;
import static agsamkin.code.service.ButtonsService.ISSUE_ORDER_OPTION_ACTION;
import static agsamkin.code.service.ButtonsService.ISSUE_PREV_PAGE_ACTION;
import static agsamkin.code.service.ButtonsService.ISSUE_SORT_ACTION;
import static agsamkin.code.service.ButtonsService.ISSUE_SORT_OPTION_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_NEXT_PAGE_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_PREV_PAGE_ACTION;
import static agsamkin.code.service.ButtonsService.REMOVE_LANGUAGE_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_ORDER_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_ORDER_OPTION_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_SORT_ACTION;
import static agsamkin.code.service.ButtonsService.REPO_SORT_OPTION_ACTION;
import static agsamkin.code.service.ButtonsService.SETTING_ACTION;
import static agsamkin.code.service.ButtonsService.SETTING_BACK_ACTION;
import static agsamkin.code.service.ButtonsService.SET_LANGUAGE_ACTION;

import static agsamkin.code.service.GitHubService.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;
import static agsamkin.code.service.SendMessageService.PAGE_DEFAULT;
import static agsamkin.code.service.SendMessageService.SETTINGS_MESSAGE;
import static agsamkin.code.service.SendMessageService.SIZE_DEFAULT;

@RequiredArgsConstructor
@Component
public class CallbackQueryHandler {
    public static final String REPO_SORT_MESSAGE = "Sort repository:";
    public static final String REPO_ORDER_MESSAGE = "Order repository:";

    private static final String ISSUE_SORT_MESSAGE = "Sort issue:";
    public static final String ISSUE_ORDER_MESSAGE = "Order issue:";

    private final LanguageService languageService;
    private final UserService userService;
    private final ButtonsService buttonsService;
    private final RepoService repoService;
    private final IssueService issueService;
    private final SendMessageUtil sendMessageUtil;
    private final TgBot tgBot;

    @SneakyThrows
    public SendMessage handleCallback(CallbackQuery callbackQuery) {
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
            Language language = languageService.getLanguageByName(value);
            userService.setLanguage(userId, language);

            EditMessageReplyMarkup editMessage = EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .replyMarkup(buttonsService.getSetupMyLanguageButtons(userId)).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REMOVE_LANGUAGE_ACTION.equals(action)) {
            Language language = languageService.getLanguageByName(value);
            userService.removeLanguage(userId, language);

            EditMessageReplyMarkup editMessage = EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .replyMarkup(buttonsService.getSetupMyLanguageButtons(userId)).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REPO_NEXT_PAGE_ACTION.equals(action)
                || REPO_PREV_PAGE_ACTION.equals(action)) {

            int page = Integer.parseInt(value);

            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();

            List<Repo> repos = repoService.getAllReposByLanguages(
                    userService.getUserLanguages(userId)
                    , PageRequest.of(page, SIZE_DEFAULT
                            , Sort.by(setting.getRepoOrder().getDirection(), setting.getRepoSort().getSortProperty())));

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(sendMessageUtil.getMarkDownTextForReposMessage(repos))
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(buttonsService.getReposButtons(
                            page, SIZE_DEFAULT, repos.size() < SIZE_DEFAULT)
                    ).build();

            tgBot.execute(editMessage);

            return null;
        } else if (REPO_SORT_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(REPO_SORT_MESSAGE)
                    .replyMarkup(buttonsService.getRepoSortButtons(user.getSetting())).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REPO_SORT_OPTION_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setRepoSort(RepoSort.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(REPO_SORT_MESSAGE)
                    .replyMarkup(buttonsService.getRepoSortButtons(setting)).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REPO_ORDER_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(REPO_ORDER_MESSAGE)
                    .replyMarkup(buttonsService.getRepoOrderButtons(user.getSetting())).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REPO_ORDER_OPTION_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setRepoOrder(RepoOrder.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(REPO_ORDER_MESSAGE)
                    .replyMarkup(buttonsService.getRepoOrderButtons(setting)).build();
            tgBot.execute(editMessage);

            return null;
        } else if (ISSUE_NEXT_PAGE_ACTION.equals(action)
                || ISSUE_PREV_PAGE_ACTION.equals(action)) {

            int page = Integer.parseInt(value);

            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();

            List<Repo> repos = repoService.getAllReposByLanguages(
                    userService.getUserLanguages(userId)
                    , PageRequest.of(0, MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT));

            List<Issue> issues = issueService.getAllIssuesByRepos(repos,
                    PageRequest.of(page, SIZE_DEFAULT
                            , Sort.by(setting.getIssueOrder().getDirection(), setting.getIssueSort().getSortProperty())));

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(sendMessageUtil.getMarkDownTextForIssuesMessage(issues))
                    .disableWebPagePreview(true)
                    .parseMode(ParseMode.MARKDOWN)
                    .replyMarkup(buttonsService.getIssuesButtons(
                            page, SIZE_DEFAULT, issues.size() < SIZE_DEFAULT)
                    ).build();

            tgBot.execute(editMessage);
            return null;
        } else if (ISSUE_SORT_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(ISSUE_SORT_MESSAGE)
                    .replyMarkup(buttonsService.getIssueSortButtons(user.getSetting())).build();
            tgBot.execute(editMessage);
            return null;
        } else if (ISSUE_SORT_OPTION_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setIssueSort(IssueSort.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(ISSUE_SORT_MESSAGE)
                    .replyMarkup(buttonsService.getIssueSortButtons(setting)).build();
            tgBot.execute(editMessage);
            return null;
        } else if (ISSUE_ORDER_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(ISSUE_ORDER_MESSAGE)
                    .replyMarkup(buttonsService.getIssueOrderButtons(user.getSetting())).build();
            tgBot.execute(editMessage);
            return null;
        } else if (ISSUE_ORDER_OPTION_ACTION.equals(action)) {
            User user = userService.getUserByUserId(userId);
            Setting setting = user.getSetting();
            setting.setIssueOrder(IssueOrder.valueOf(value));
            user.setSetting(setting);
            userService.saveUser(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .text(ISSUE_ORDER_MESSAGE)
                    .replyMarkup(buttonsService.getIssueOrderButtons(setting)).build();
            tgBot.execute(editMessage);

            return null;
        } else if (SETTING_BACK_ACTION.equals(action)) {
            if (SETTING_ACTION.equals(value)) {
                EditMessageText editMessage = EditMessageText.builder()
                        .chatId(message.getChatId())
                        .messageId(message.getMessageId())
                        .text(SETTINGS_MESSAGE)
                        .replyMarkup(buttonsService.getSettingsButtons()).build();
                tgBot.execute(editMessage);
            }
            return null;
        } else {
            return null;
        }
    }
}
