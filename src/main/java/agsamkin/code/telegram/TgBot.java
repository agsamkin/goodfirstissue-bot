package agsamkin.code.telegram;

import agsamkin.code.telegram.handler.UpdateHandler;
import lombok.Getter;
import lombok.Setter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
public class TgBot extends SpringWebhookBot {
    private String botPath;
    private String botUsername;

    private final UpdateHandler updateHandler;

    public TgBot(SetWebhook setWebhook, String botToken, UpdateHandler updateHandler) {
        super(setWebhook, botToken);
        this.updateHandler = updateHandler;
        setBotCommandList();
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandler.handleUpdate(update);
    }

    @SneakyThrows
    private void setBotCommandList() {
        List<BotCommand> commands = new ArrayList<>();
        Arrays.stream(agsamkin.code.telegram.BotCommand.values())
                .filter(command -> command != agsamkin.code.telegram.BotCommand.START)
                .forEach(command -> commands.add(new BotCommand(command.getName(), command.getDescription())));
        this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
    }
}
