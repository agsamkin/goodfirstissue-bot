package agsamkin.code;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableRabbit
@EnableAsync
@SpringBootApplication
public class GoodFirstIssueBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodFirstIssueBotApplication.class, args);
    }
}
