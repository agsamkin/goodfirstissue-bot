package agsamkin.code.telegram.handler;

import agsamkin.code.service.SendMessageService;
import agsamkin.code.service.UserService;
import agsamkin.code.telegram.BotCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@Component
public class MessageHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;

    public SendMessage handleMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        if (BotCommand.START.getName().equals(text)) {
            userService.registerUser(message);
            return sendMessageService.getLanguageSelectMessage(chatId);
        } else if (BotCommand.SELECT_LANGUAGES.getName().equals(text)) {
            return sendMessageService.getLanguageSelectMessage(chatId);
        } else if (BotCommand.SETTINGS.getName().equals(text)) {
            return sendMessageService.getSimpleMessage(chatId, "Settings");
        } else if (BotCommand.HELP.getName().equals(text)) {
            return sendMessageService.getSimpleMessage(chatId, "Help, I need somebody...");
        } else {
            return sendMessageService.getSimpleMessage(chatId, "Unsupported command");
        }
    }
}
