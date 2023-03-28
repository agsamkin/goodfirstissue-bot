package agsamkin.code.service.impl;

import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.SendMessageService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@RequiredArgsConstructor
@Service
public class SendMessageServiceImpl implements SendMessageService {
    private static final String LANGUAGE_SELECT_MESSAGE = "Select languages from the list below:";

    private final ButtonsService buttonsService;

    @Override
    public SendMessage getSimpleMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    @Override
    public SendMessage getLanguageSelectMessage(Long chatId) {
        SendMessage sendMessage = getSimpleMessage(chatId, LANGUAGE_SELECT_MESSAGE);
        sendMessage.setReplyMarkup(buttonsService.getLanguageSelectButtons());
        return sendMessage;
    }
}
