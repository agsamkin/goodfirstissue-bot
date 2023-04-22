package agsamkin.code.service.impl;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import agsamkin.code.model.User;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.IssueService;
import agsamkin.code.service.RepoService;
import agsamkin.code.service.SendMessageService;
import agsamkin.code.service.UserService;
import agsamkin.code.telegram.BotCommand;
import agsamkin.code.util.SendMessageUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static agsamkin.code.util.GitHubUtil.MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT;
import static agsamkin.code.util.SendMessageUtil.DASH;
import static agsamkin.code.util.SendMessageUtil.GREETING_MESSAGE;
import static agsamkin.code.util.SendMessageUtil.ISSUES_PAGE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.ISSUES_PAGE_SIZE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.LINE_BREAK;
import static agsamkin.code.util.SendMessageUtil.REPOS_PAGE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.REPOS_PAGE_SIZE_DEFAULT;
import static agsamkin.code.util.SendMessageUtil.SETTINGS_MESSAGE;
import static agsamkin.code.util.SendMessageUtil.SETUP_MY_LANGUAGES_MESSAGE;
import static agsamkin.code.util.SendMessageUtil.UNSUPPORTED_COMMAND_TEXT;

@RequiredArgsConstructor
@Service
public class SendMessageServiceImpl implements SendMessageService {
    private final SendMessageUtil sendMessageUtil;
    private final ButtonsService buttonsService;
    private final UserService userService;
    private final RepoService repoService;
    private final IssueService issueService;

    @Override
    public SendMessage getGreetingMessage(Long chatId) {
        StringBuilder sb = new StringBuilder(GREETING_MESSAGE);

        String botCommands = Arrays.stream(BotCommand.values())
                .filter(BotCommand::isShowInMenu)
                .map(bc -> bc.getName() + DASH + bc.getDescription())
                .collect(Collectors.joining(LINE_BREAK));

        sb.append(botCommands);

        return sendMessageUtil.getSimpleMessage(chatId, sb.toString());
    }

    @Override
    public SendMessage getSettingsMessage(Long chatId) {
        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(chatId, SETTINGS_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getSettingsButtons());
        return sendMessage;
    }

    @Override
    public SendMessage getHelpMessage(Long chatId) {
        return getGreetingMessage(chatId);
    }

    @Override
    public SendMessage getSetupMyLanguageMessage(Long chatId, Long userId) {
        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(chatId, SETUP_MY_LANGUAGES_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getSetupMyLanguageButtons(userId));
        return sendMessage;
    }

    @Override
    public SendMessage getReposMessage(Long chatId, Long userId) {
        User user = userService.getUserByUserId(userId);
        Setting setting = user.getSetting();

        List<Repo> repos = repoService.getAllReposByLanguages(
                userService.getUserLanguages(userId)
                , PageRequest.of(REPOS_PAGE_DEFAULT, REPOS_PAGE_SIZE_DEFAULT
                        , Sort.by(setting.getRepoOrder().getDirection(), setting.getRepoSort().getSortProperty())));

        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(
                chatId, sendMessageUtil.getMarkDownTextForReposMessage(repos));

        sendMessage.setDisableWebPagePreview(true);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(
                buttonsService.getReposButtons(
                        REPOS_PAGE_DEFAULT, REPOS_PAGE_SIZE_DEFAULT, repos.size() < REPOS_PAGE_SIZE_DEFAULT)
        );
        return sendMessage;
    }

    @Override
    public SendMessage getIssuesMessage(Long chatId, Long userId) {
        User user = userService.getUserByUserId(userId);
        Setting setting = user.getSetting();

        List<Repo> repos = repoService.getAllReposByLanguages(
                userService.getUserLanguages(userId), PageRequest.of(0, MAX_NUMBER_OF_REPOS_IN_QUERY_RESULT));

        List<Issue> issues = issueService.getAllIssuesByRepos(repos,
                PageRequest.of(ISSUES_PAGE_DEFAULT, ISSUES_PAGE_SIZE_DEFAULT
                        , Sort.by(setting.getIssueOrder().getDirection(), setting.getIssueSort().getSortProperty())));

        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(
                chatId, sendMessageUtil.getMarkDownTextForIssuesMessage(issues));

        sendMessage.setDisableWebPagePreview(true);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setReplyMarkup(
                buttonsService.getIssuesButtons(
                        ISSUES_PAGE_DEFAULT, ISSUES_PAGE_SIZE_DEFAULT, issues.size() < ISSUES_PAGE_SIZE_DEFAULT)
        );
        return sendMessage;
    }

    @Override
    public SendMessage getUnsupportedCommandMessage(Long chatId) {
        return sendMessageUtil.getSimpleMessage(chatId, UNSUPPORTED_COMMAND_TEXT);
    }
}
