package agsamkin.code.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface SendMessageService {
    SendMessage getGreetingMessage(Long chatId);
    SendMessage getSettingsMessage(Long chatId);
    SendMessage getHelpMessage(Long chatId);

    SendMessage getSetupMyLanguageMessage(Long chatId, Long userId);
    SendMessage getReposMessage(Long chatId, Long userId);
    SendMessage getIssuesMessage(Long chatId, Long userId);

    SendMessage getUnsupportedCommandMessage(Long chatId);
}
