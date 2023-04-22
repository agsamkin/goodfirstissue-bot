package agsamkin.code.rabbit;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateConsumer {
    void consumeTextMessage(Update update);
    void consumeCallbackQuery(Update update);
}
