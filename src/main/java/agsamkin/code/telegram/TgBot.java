package agsamkin.code.telegram;

import agsamkin.code.config.TgBotConfig;
import agsamkin.code.telegram.handler.UpdateHandler;
import lombok.Getter;
import lombok.Setter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Setter
@Component
public class TgBot extends SpringWebhookBot {
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
        if (sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(String.valueOf(e));
            }
        }
    }
}
