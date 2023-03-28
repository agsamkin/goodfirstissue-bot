package agsamkin.code.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendMessageService {
    SendMessage getSimpleMessage(Long chatId, String text);
    SendMessage getLanguageSelectMessage(Long chatId);
}
