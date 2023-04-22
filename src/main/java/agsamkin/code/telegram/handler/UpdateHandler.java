package agsamkin.code.telegram.handler;

import agsamkin.code.rabbit.UpdateProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

import static agsamkin.code.config.RabbitConfig.CALLBACK_QUERY_QUEUE;
import static agsamkin.code.config.RabbitConfig.TEXT_MESSAGE_QUEUE;

@RequiredArgsConstructor
@Component
public class UpdateHandler {
    private final UpdateProducer updateProducer;

    @Async
    public void handleUpdate(Update update) {
        if (Objects.isNull(update)) {
            return;
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        } else {
            return;
        }
    }

    private void handleMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_QUEUE, update);
    }

    private void handleCallback(Update update) {
        updateProducer.produce(CALLBACK_QUERY_QUEUE, update);
    }
}
