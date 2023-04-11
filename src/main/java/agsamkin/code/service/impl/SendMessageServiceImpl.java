package agsamkin.code.service.impl;

import agsamkin.code.model.Language;

import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.ScheduleService;
import agsamkin.code.service.SendMessageService;

import agsamkin.code.service.UserService;
import agsamkin.code.telegram.BotCommand;
import agsamkin.code.util.SendMessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Arrays;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SendMessageServiceImpl implements SendMessageService {
    public static final String LINE_BREAK = "\n";
    public static final String BOT_COMMAND_SEPARATOR = " - ";

    public static final String GREETING_MESSAGE =
            "I can help you find good first issues to get you started contributing to open source."
                    + StringUtils.repeat(LINE_BREAK, 2)
                    + "You can control me by sending these commands:"
                    + StringUtils.repeat(LINE_BREAK, 2);

    public static final String SETUP_MY_LANGUAGES_MESSAGE = "Set languages from the list below:";
    public static final String MY_LANGUAGES_MESSAGE =
            "Your languages is [%s]. To change the languages, please use "
                    + BotCommand.SETUP_MY_LANGUAGES.getName() + " command.";

    public static final String UNSUPPORTED_COMMAND_TEXT = "Unsupported command";

    public static final String NONE_USER_LANGUAGES = "None";

    private final SendMessageUtil sendMessageUtil;

    private final ButtonsService buttonsService;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Override
    public SendMessage getGreetingMessage(Long chatId) {
        StringBuilder sb = new StringBuilder(GREETING_MESSAGE);

        String botCommands = Arrays.stream(BotCommand.values())
                .filter(BotCommand::isShowInMenu)
                .map(bc -> bc.getName() + BOT_COMMAND_SEPARATOR + bc.getDescription())
                .collect(Collectors.joining(LINE_BREAK));

        sb.append(botCommands);

        return sendMessageUtil.getSimpleMessage(chatId, sb.toString());
    }

    @Override
    public SendMessage getSetupMyLanguageMessage(Long chatId, Long userId) {
        SendMessage sendMessage = sendMessageUtil.getSimpleMessage(chatId, SETUP_MY_LANGUAGES_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getSetupMyLanguageButtons(userId));
        return sendMessage;
    }

    @Override
    public SendMessage getMyLanguageMessage(Long chatId, Long userId) {
        List<Language> languages = userService.getUserLanguages(userId);
        languages.sort(Comparator.comparing(Language::getName));

        String userLanguages = NONE_USER_LANGUAGES;
        if (languages.size() != 0) {
            userLanguages = languages.stream()
                    .map(l -> l.getName())
                    .collect(Collectors.joining(", "));
        }

        String text = MY_LANGUAGES_MESSAGE.formatted(userLanguages);

        return sendMessageUtil.getSimpleMessage(chatId, text);
    }

    @Async
    @SneakyThrows
    @Override
    public SendMessage doTest(Long chatId, Long userId) {
        scheduleService.deleteRepos();
        return null;
    }

    @Override
    public SendMessage getUnsupportedCommandMessage(Long chatId) {
        return sendMessageUtil.getSimpleMessage(chatId, UNSUPPORTED_COMMAND_TEXT);
    }
}
