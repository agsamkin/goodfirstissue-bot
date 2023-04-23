package agsamkin.code.config;

import lombok.Getter;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.RateLimitChecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static agsamkin.code.util.GitHubUtil.MIN_REMAINING_NUMBER_OF_REQUEST;

@Getter
@Configuration
public class GitHubConfig {
    @Value("${github_token}")
    private String gitHubToken;

    @Bean
    public GitHub gitHub() throws IOException {
        return new GitHubBuilder()
                .withJwtToken(gitHubToken)
                .withRateLimitChecker(
                        new RateLimitChecker.LiteralValue(MIN_REMAINING_NUMBER_OF_REQUEST)
                ).build();
    }
}
