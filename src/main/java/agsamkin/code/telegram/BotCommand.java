package agsamkin.code.telegram;

import lombok.Getter;

@Getter
public enum BotCommand {
    START("/start", "Start bot")
    , SETTINGS("/settings", "Bot settings")
    , HELP("/help", "Help");

    private final String name;
    private final String description;

    BotCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
