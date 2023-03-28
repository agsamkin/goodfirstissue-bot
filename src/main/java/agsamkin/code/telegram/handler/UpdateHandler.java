package agsamkin.code.telegram.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class UpdateHandler {
    private final MessageHandler messageHandler;

    public SendMessage handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            return messageHandler.handleMessage(update.getMessage());
        }
        return null;
    }
}
