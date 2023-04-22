package agsamkin.code.service;

import agsamkin.code.model.setting.Setting;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public interface ButtonsService {
    InlineKeyboardMarkup getSetupMyLanguageButtons(Long userId);

    InlineKeyboardMarkup getReposButtons(int page, int size, boolean isLastPage);
    InlineKeyboardMarkup getIssuesButtons(int page, int size, boolean isLastPage);

    InlineKeyboardMarkup getSettingsButtons();
    InlineKeyboardMarkup getRepoSortButtons(Setting setting);
    InlineKeyboardMarkup getRepoSortOrderButtons(Setting setting);
    InlineKeyboardMarkup getIssueSortButtons(Setting setting);
    InlineKeyboardMarkup getIssueSortOrderButtons(Setting setting);
}
