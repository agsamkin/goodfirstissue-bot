package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.LanguageService;
import agsamkin.code.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ButtonsServiceImpl implements ButtonsService {
    public static final int LANGUAGES_PER_ROW = 3;
    public static final String ACTION_SEPARATOR = ":";
    public static final String SET_LANGUAGE_ACTION = "set_lang";
    public static final String REMOVE_LANGUAGE_ACTION = "remove_lang";
    public static final String MARK = "✅";

    private final LanguageService languageService;
    private final UserService userService;

    @Override
    public InlineKeyboardMarkup getSetupMyLanguageButtons(long userId) {
        List<Language> allLanguages = languageService.getLanguagesByEnable(true);
        allLanguages.sort(Comparator.comparing(Language::getName));

        List<Language> userLanguages = userService.getLanguagesByUserId(userId);

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsRow = new ArrayList<>();

        int count = 0;
        for (Language language : allLanguages) {
            if (count >= LANGUAGES_PER_ROW) {
                buttons.add(buttonsRow);
                buttonsRow = new ArrayList<>();
                count = 0;
            }

            String text = language.getName();
            String callbackData = ACTION_SEPARATOR + language.getName();

            if (userLanguages.size() > 0 && userLanguages.contains(language)) {
                text = MARK + text;
                callbackData = REMOVE_LANGUAGE_ACTION + callbackData;
            } else {
                callbackData = SET_LANGUAGE_ACTION + callbackData;
            }

            buttonsRow.add(InlineKeyboardButton.builder()
                    .text(text)
                    .callbackData(callbackData).build());
            count++;

        }

        if (buttonsRow.size() > 0) {
            buttons.add(buttonsRow);
        }

        return new InlineKeyboardMarkup(buttons);
    }
}
