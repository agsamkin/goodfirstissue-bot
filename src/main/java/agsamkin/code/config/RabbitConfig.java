package agsamkin.code.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String TEXT_MESSAGE_QUEUE = "text_message";
    public static final String CALLBACK_QUERY_QUEUE = "callback_query";

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(TEXT_MESSAGE_QUEUE);
    }

    @Bean
    public Queue callbackQueryQueue() {
        return new Queue(CALLBACK_QUERY_QUEUE);
    }
}
