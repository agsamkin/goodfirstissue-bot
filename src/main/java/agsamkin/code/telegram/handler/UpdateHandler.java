package agsamkin.code.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class UpdateHandler {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    public SendMessage handleUpdate(Update update) {
        if (Objects.isNull(update)) {
            return null;
        }

        if (update.hasMessage() && update.getMessage().getFrom().getId() != 617933778L) {
            return null;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            return messageHandler.handleMessage(update.getMessage());
        } else if (update.hasCallbackQuery()) {
            return callbackQueryHandler.handleCallback(update.getCallbackQuery());
        } else {
            return null;
        }
    }
}
