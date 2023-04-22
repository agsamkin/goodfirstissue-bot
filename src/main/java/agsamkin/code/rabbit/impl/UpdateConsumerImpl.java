package agsamkin.code.rabbit.impl;

import agsamkin.code.rabbit.UpdateConsumer;
import agsamkin.code.telegram.handler.CallbackQueryHandler;
import agsamkin.code.telegram.handler.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static agsamkin.code.config.RabbitConfig.CALLBACK_QUERY_QUEUE;
import static agsamkin.code.config.RabbitConfig.TEXT_MESSAGE_QUEUE;

@RequiredArgsConstructor
@Service
public class UpdateConsumerImpl implements UpdateConsumer {
    private final MessageHandler messageHandler;
    private final CallbackQueryHandler callbackQueryHandler;

    @RabbitListener(queues = TEXT_MESSAGE_QUEUE)
    @Override
    public void consumeTextMessage(Update update) {
        messageHandler.handleMessage(update.getMessage());
    }

    @RabbitListener(queues = CALLBACK_QUERY_QUEUE)
    @Override
    public void consumeCallbackQuery(Update update) {
        callbackQueryHandler.handleCallback(update.getCallbackQuery());
    }
}
