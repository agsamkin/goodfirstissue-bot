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
    public final String UNSUPPORTED_COMMAND_TEXT = "Unsupported command";

    private final UserService userService;
    private final SendMessageService sendMessageService;

    public SendMessage handleMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();

        if (BotCommand.START.getName().equals(text)) {
            userService.registerUser(message);
            return sendMessageService.getGreetingMessage(chatId);
        } else if (BotCommand.SETTINGS.getName().equals(text)) {
            return sendMessageService.getSimpleMessage(chatId, text);
        } else if (BotCommand.HELP.getName().equals(text)) {
            return sendMessageService.getSimpleMessage(chatId, text);

        } else if (BotCommand.SETUP_MY_LANGUAGES.getName().equals(text)) {
            return sendMessageService.getSetupMyLanguageMessage(chatId, userId);
        } else if (BotCommand.MY_LANGUAGES.getName().equals(text)) {
            return sendMessageService.getMyLanguageMessage(chatId, userId);

        } else {
            return sendMessageService.getSimpleMessage(chatId, UNSUPPORTED_COMMAND_TEXT);
        }
    }
}
