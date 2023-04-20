package agsamkin.code.service;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import agsamkin.code.model.Repo;
import agsamkin.code.model.User;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.telegram.BotCommand;
import agsamkin.code.util.SendMessageUtil;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static agsamkin.code.service.GitHubService.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;

@RequiredArgsConstructor
@Service
public class SendMessageService {
    public static final String LINE_BREAK = "\n";
    public static final String BOT_COMMAND_SEPARATOR = " - ";

    public static final String GREETING_MESSAGE =
            "I can help you find good first issues to get you started contributing to open source."
                    + StringUtils.repeat(LINE_BREAK, 2)
                    + "You can control me by sending these commands:"
                    + StringUtils.repeat(LINE_BREAK, 2);

    public static final String SETUP_MY_LANGUAGES_MESSAGE = "Set languages from the list below:";
    public static final String REPOS_MESSAGE = "Repos with good-first-issue label:";
    public static final String ISSUES_MESSAGE = "Issues with good-first-issue label:";
    public static final String SETTINGS_MESSAGE = "⚙ Settings:";
    public static final String UNSUPPORTED_COMMAND_TEXT = "Unsupported command";

    public static final int PAGE_DEFAULT = 0;
    public static final int SIZE_DEFAULT = 10;
    public static final String REPOS_SORT_PROPERTY = "updatedAt";
    public static final String ISSUES_SORT_PROPERTY = "updatedAt";

    private final SendMessageUtil sendMessageUtil;
    private final ButtonsService buttonsService;
    private final UserService userService;
    private final RepoService repoService;
    private final IssueService issueService;
    private final ScheduleService scheduleService;

    public SendMessage getGreetingMessage(Long chatId) {
        StringBuilder sb = new StringBuilder(GREETING_MESSAGE);

        String botCommands = Arrays.stream(BotCommand.values())
                .filter(BotCommand::isShowInMenu)
                .map(bc -> bc.getName() + BOT_COMMAND_SEPARATOR + bc.getDescription())
                .collect(Collectors.joining(LINE_BREAK));

        sb.append(botCommands);

        return sendMessageUtil.getSimpleMessage(chatId, sb.toString());
    }

    public SendMessage getSetupMyLanguageMessage(Long chatId, Long userId) {
        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(chatId, SETUP_MY_LANGUAGES_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getSetupMyLanguageButtons(userId));
        return sendMessage;
    }

    public SendMessage getReposMessage(Long chatId, Long userId) {
        User user = userService.getUserByUserId(userId);
        Setting setting = user.getSetting();

        List<Repo> repos = repoService.getAllReposByLanguages(
                userService.getUserLanguages(userId)
                , PageRequest.of(PAGE_DEFAULT, SIZE_DEFAULT
                        , Sort.by(setting.getRepoOrder().getDirection(), setting.getRepoSort().getSortProperty())));

        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(
                chatId, sendMessageUtil.getMarkDownTextForReposMessage(repos));

        sendMessage.setDisableWebPagePreview(true);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(
                buttonsService.getReposButtons(
                        PAGE_DEFAULT, SIZE_DEFAULT, repos.size() < SIZE_DEFAULT)
        );
        return sendMessage;
    }

    public SendMessage getIssuesMessage(Long chatId, Long userId) {
        User user = userService.getUserByUserId(userId);
        Setting setting = user.getSetting();

        List<Repo> repos = repoService.getAllReposByLanguages(
                userService.getUserLanguages(userId), PageRequest.of(0, MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT));

        List<Issue> issues = issueService.getAllIssuesByRepos(repos,
                PageRequest.of(PAGE_DEFAULT, SIZE_DEFAULT
                        , Sort.by(setting.getIssueOrder().getDirection(), setting.getIssueSort().getSortProperty())));

        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(
                chatId, sendMessageUtil.getMarkDownTextForIssuesMessage(issues));

        sendMessage.setDisableWebPagePreview(true);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(
                buttonsService.getIssuesButtons(
                        PAGE_DEFAULT, SIZE_DEFAULT, issues.size() < SIZE_DEFAULT)
        );
        return sendMessage;
    }

    public SendMessage getSettingsMessage(Long chatId) {
        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(chatId, SETTINGS_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getSettingsButtons());
        return sendMessage;
    }

    public SendMessage getUnsupportedCommandMessage(Long chatId) {
        return sendMessageUtil.getSimpleMessage(chatId, UNSUPPORTED_COMMAND_TEXT);
    }
}
