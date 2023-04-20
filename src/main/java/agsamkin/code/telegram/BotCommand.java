package agsamkin.code.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotCommand {
    START("/start", "Start bot", false)
    , SETTINGS("/settings", "Show settings", true)
    , SETUP_MY_LANGUAGES("/setmylang", "Setup own languages", true)
    , REPOS("/repos", "Get repos", true)
    , ISSUES("/issues", "Get issues", true)
    , HELP("/help", "Show help", false);

    private final String name;
    private final String description;
    private final boolean showInMenu;
}
