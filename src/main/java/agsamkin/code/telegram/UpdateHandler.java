package agsamkin.code.telegram;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class UpdateHandler {

    public SendMessage handleUpdate(Update update) {
        return null;
    }
}
