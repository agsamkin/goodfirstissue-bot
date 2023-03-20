package agsamkin.code.config;

import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GitHubConfig {

    @Value("${github_token}")
    private String gitHubToken;

    @Bean
    public GitHub gitHub() throws IOException {
        return new GitHubBuilder()
                .withJwtToken(gitHubToken).build();
    }
}