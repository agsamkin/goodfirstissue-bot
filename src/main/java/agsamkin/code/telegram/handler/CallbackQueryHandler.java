package agsamkin.code.telegram.handler;

import agsamkin.code.model.Language;
import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.UserService;
import agsamkin.code.telegram.TgBot;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import static agsamkin.code.service.impl.ButtonsServiceImpl.REMOVE_LANGUAGE_ACTION;
import static agsamkin.code.service.impl.ButtonsServiceImpl.SET_LANGUAGE_ACTION;
import static agsamkin.code.service.impl.ButtonsServiceImpl.ACTION_SEPARATOR;

@RequiredArgsConstructor
@Component
public class CallbackQueryHandler {
    private final LanguageService languageService;
    private final UserService userService;
    private final ButtonsService buttonsService;
    private final TgBot tgBot;

    @SneakyThrows
    public SendMessage handleCallback(CallbackQuery callbackQuery) {
        long userId = callbackQuery.getFrom().getId();
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();

        String[] params = data.split(ACTION_SEPARATOR);

        String action = params[0];
        String value = params[1];

        if (SET_LANGUAGE_ACTION.equals(action)) {
            Language language = languageService.getLanguageByName(value);
            userService.setLanguage(userId, language);

            EditMessageReplyMarkup editMessage = EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .replyMarkup(buttonsService.getSetupMyLanguageButtons(userId)).build();
            tgBot.execute(editMessage);
            return null;
        } else if (REMOVE_LANGUAGE_ACTION.equals(action)) {
            Language language = languageService.getLanguageByName(value);
            userService.removeLanguage(userId, language);

            EditMessageReplyMarkup editMessage = EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .replyMarkup(buttonsService.getSetupMyLanguageButtons(userId)).build();
            tgBot.execute(editMessage);
            return null;
        } else {
            return null;
        }
    }
}
