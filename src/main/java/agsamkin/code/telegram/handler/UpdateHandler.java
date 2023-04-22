package agsamkin.code.telegram.handler;

import agsamkin.code.rabbit.UpdateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static agsamkin.code.config.RabbitConfig.CALLBACK_QUERY_QUEUE;
import static agsamkin.code.config.RabbitConfig.TEXT_MESSAGE_QUEUE;

@RequiredArgsConstructor
@Component
public class UpdateHandler {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    private final UpdateProducer updateProducer;

    @Async
    public SendMessage handleUpdate(Update update) {
        if (Objects.isNull(update)) {
            return null;
        }

        if (update.hasMessage() && update.getMessage().getFrom().getId() != 617933778L) {
            return null;
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
//            return messageHandler.handleMessage(update.getMessage());
            handleMessage(update);
            return null;
        } else if (update.hasCallbackQuery()) {
//            return callbackQueryHandler.handleCallback(update.getCallbackQuery());
            handleCallback(update);
            return null;
        } else {
            return null;
        }
    }

    private void handleMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_QUEUE, update);
    }

    private void handleCallback(Update update) {
        updateProducer.produce(CALLBACK_QUERY_QUEUE, update);
    }
}
