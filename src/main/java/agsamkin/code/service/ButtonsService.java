package agsamkin.code.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface ButtonsService {
    InlineKeyboardMarkup getSetupMyLanguageButtons(Long userId);
}
