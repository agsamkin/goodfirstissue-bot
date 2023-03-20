package agsamkin.code.telegram;

import lombok.Getter;
import lombok.Setter;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
public class TgBot extends SpringWebhookBot {

    private String botPath;
    private String botUsername;

    private final UpdateHandler updateHandler;

    public TgBot(SetWebhook setWebhook, String botToken, UpdateHandler updateHandler) {
        super(setWebhook, botToken);
        this.updateHandler = updateHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandler.handleUpdate(update);
    }
}
