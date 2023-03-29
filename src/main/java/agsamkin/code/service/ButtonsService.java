package agsamkin.code.service;

import agsamkin.code.model.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.List;

public interface ButtonsService {
    InlineKeyboardMarkup getSetupMyLanguageButtons(long userId);
}
