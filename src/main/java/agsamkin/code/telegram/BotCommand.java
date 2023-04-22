package agsamkin.code.telegram;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BotCommand {
    START("/start", "start bot", false)
    , SETUP_MY_LANGUAGES("/setmylang", "setup own languages", true)
    , REPOS("/repos", "get repositories", true)
    , ISSUES("/issues", "get issues", true)
    , SETTINGS("/settings", "show settings", true)
    , HELP("/help", "show help", true);

    private final String name;
    private final String description;
    private final boolean showInMenu;
}
