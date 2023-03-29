package agsamkin.code.telegram;

import agsamkin.code.config.TgBotConfig;
import agsamkin.code.telegram.handler.UpdateHandler;
import lombok.Getter;
import lombok.Setter;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Component
public class TgBot extends SpringWebhookBot {
    private final TgBotConfig tgBotConfig;
    private final UpdateHandler updateHandler;

    public TgBot(SetWebhook setWebhook, TgBotConfig tgBotConfig, @Lazy UpdateHandler updateHandler) {
        super(setWebhook, tgBotConfig.getBotToken());
        this.tgBotConfig = tgBotConfig;
        this.updateHandler = updateHandler;
        setBotCommandList();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandler.handleUpdate(update);
    }

    @Override
    public String getBotPath() {
        return tgBotConfig.getBotPath();
    }

    @Override
    public String getBotUsername() {
        return tgBotConfig.getBotUsername();
    }

    @SneakyThrows
    private void setBotCommandList() {
        List<BotCommand> commands = new ArrayList<>();
        Arrays.stream(agsamkin.code.telegram.BotCommand.values())
                .filter(agsamkin.code.telegram.BotCommand::isShowInMenu)
                .forEach(command -> commands.add(new BotCommand(command.getName(), command.getDescription())));
        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }
}
