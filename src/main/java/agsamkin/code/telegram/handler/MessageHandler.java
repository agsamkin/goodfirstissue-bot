package agsamkin.code.telegram.handler;

import agsamkin.code.model.User;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.service.SendMessageService;
import agsamkin.code.service.UserService;
import agsamkin.code.telegram.BotCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class MessageHandler {
    private final UserService userService;
    private final SendMessageService sendMessageService;

    public SendMessage handleMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        Long userId = message.getFrom().getId();

        if (BotCommand.START.getName().equals(text)) {
            User user = User.builder()
                    .userId(message.getFrom().getId())
                    .userName(message.getFrom().getUserName())
                    .firstName(message.getFrom().getFirstName())
                    .lastName(message.getFrom().getLastName())
                    .languageCode(message.getFrom().getLanguageCode()).build();

            Setting setting = Setting.builder()
                    .user(user)
                    .repoSort(RepoSort.UPDATED)
                    .repoOrder(RepoOrder.DESC).build();
            user.setSetting(setting);
            
            userService.registerUser(user);
            return sendMessageService.getGreetingMessage(chatId);
        } else if (BotCommand.SETTINGS.getName().equals(text)) {
            return sendMessageService.getSettingsMessage(chatId);
        } else if (BotCommand.HELP.getName().equals(text)) {
            return sendMessageService.getUnsupportedCommandMessage(chatId);

        } else if (BotCommand.SETUP_MY_LANGUAGES.getName().equals(text)) {
            return sendMessageService.getSetupMyLanguageMessage(chatId, userId);
        } else if (BotCommand.REPOS.getName().equals(text)) {
            return sendMessageService.getReposMessage(chatId, userId);
        } else if (BotCommand.ISSUES.getName().equals(text)) {
            return sendMessageService.getIssuesMessage(chatId, userId);
        } else {
            return sendMessageService.getUnsupportedCommandMessage(chatId);
        }
    }
}
