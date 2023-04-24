package agsamkin.code.telegram;

import agsamkin.code.config.TgBotConfig;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Component
public class TgBot extends SpringWebhookBot {
    public static final int EMPTY_MESSAGE_ID = 0;
    public static final String EMPTY_CHAT_ID = "0";

    private final TgBotConfig tgBotConfig;

    public TgBot(SetWebhook setWebhook, TgBotConfig tgBotConfig) {
        super(setWebhook, tgBotConfig.getBotToken());
        this.tgBotConfig = tgBotConfig;
        setBotCommandList();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return null;
    }

    @Override
    public String getBotPath() {
        return tgBotConfig.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return tgBotConfig.getBotUsername();
    }

    private void setBotCommandList() {
        List<BotCommand> commands = new ArrayList<>();
        Arrays.stream(agsamkin.code.telegram.BotCommand.values())
                .filter(agsamkin.code.telegram.BotCommand::isShowInMenu)
                .forEach(command -> commands.add(new BotCommand(command.getName(), command.getDescription())));

        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        if (sendMessage == null
                || Objects.isNull(sendMessage.getChatId())
                || sendMessage.getChatId().equals(EMPTY_CHAT_ID)) {
            return;
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }

    public void sendMessage(EditMessageReplyMarkup editMessageReplyMarkup) {
        if (editMessageReplyMarkup == null
                || editMessageReplyMarkup.getMessageId() == EMPTY_MESSAGE_ID
                || Objects.isNull(editMessageReplyMarkup.getChatId())
                || editMessageReplyMarkup.getChatId().equals(EMPTY_CHAT_ID)) {
            return;
        }
        try {
            execute(editMessageReplyMarkup);
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }

    public void sendMessage(EditMessageText editMessageText) {
        if (editMessageText == null
                || editMessageText.getMessageId() == EMPTY_MESSAGE_ID
                || Objects.isNull(editMessageText.getChatId())
                || editMessageText.getChatId().equals(EMPTY_CHAT_ID)) {
            return;
        }
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error(String.valueOf(e));
        }
    }
}
