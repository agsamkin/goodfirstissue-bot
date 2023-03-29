package agsamkin.code.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Getter
@Configuration
public class TgBotConfig {
    @Value("${telegram-bot.webhook-path}")
    private String botPath;
    @Value("${telegram-bot.bot-username}")
    private String botUsername;
    @Value("${telegram-bot.bot-token}")
    private String botToken;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(botPath).build();
    }
}
