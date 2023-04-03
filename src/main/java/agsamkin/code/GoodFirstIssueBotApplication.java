package agsamkin.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GoodFirstIssueBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(GoodFirstIssueBotApplication.class, args);
	}
}
