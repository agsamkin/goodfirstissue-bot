package agsamkin.code.telegram;

import lombok.Getter;

@Getter
public enum BotCommand {
    START("/start", "Start bot", false)
    , SETUP_MY_LANGUAGES("/setmylang", "Setup own languages", true)
    , MY_LANGUAGES("/mylang", "Check own languages", true)
    , SETTINGS("/settings", "Bot settings", false)
    , HELP("/help", "Help", false);

    private final String name;
    private final String description;
    private final boolean showInMenu;

    BotCommand(String name, String description, boolean showInMenu) {
        this.name = name;
        this.description = description;
        this.showInMenu = showInMenu;
    }
}
