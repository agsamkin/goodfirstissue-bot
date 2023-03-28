package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.service.ButtonsService;
import agsamkin.code.service.LanguageService;
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
    private static final int LANGUAGES_PER_LINE = 3;

    private final LanguageService languageService;

    @Override
    public InlineKeyboardMarkup getLanguageSelectButtons() {
        List<Language> languages = languageService.getLanguagesByEnable(true);
        languages.sort(Comparator.comparing(Language::getName));

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();

        int count = 0;
        for (Language language : languages) {
            if (count >= LANGUAGES_PER_LINE) {
                buttons.add(buttonsLine);
                buttonsLine = new ArrayList<>();
                count = 0;
            }
            buttonsLine.add(InlineKeyboardButton.builder()
                    .text(language.getName())
                    .callbackData(language.getName()).build());
            count++;
        }

        if (buttonsLine.size() > 0) {
            buttons.add(buttonsLine);
        }

        return new InlineKeyboardMarkup(buttons);
    }
}
