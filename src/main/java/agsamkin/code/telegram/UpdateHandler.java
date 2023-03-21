package agsamkin.code.telegram;

import agsamkin.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@Component
public class UpdateHandler {

    private final UserService userService;

    public SendMessage handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (BotCommand.START.getName().equals(text)) {
                userService.registerUser(update.getMessage());
                return getSendMessage(chatId, "Hello @" + update.getMessage().getFrom().getUserName());
            } else if (BotCommand.SETTINGS.getName().equals(text)) {
                return getSendMessage(chatId, "Settings");
            } else if (BotCommand.HELP.getName().equals(text)) {
                return getSendMessage(chatId, "Help, I need somebody...");
            } else {
                return getSendMessage(chatId, "Unsupported command");
            }
        }
        return null;
    }

    private SendMessage getSendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

}
