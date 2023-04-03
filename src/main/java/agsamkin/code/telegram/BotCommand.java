package agsamkin.code.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotCommand {
    START("/start", "Start bot", false)
    , SETTINGS("/settings", "Show settings", false)
    , SETUP_MY_LANGUAGES("/setmylang", "Setup own languages", true)
    , MY_LANGUAGES("/mylang", "Check own languages", true)
    , TEST("/test", "For test", true)
    , HELP("/help", "Show help", false);

    private final String name;
    private final String description;
    private final boolean showInMenu;
}
