package agsamkin.code.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile(SpringConfigForIT.TEST_PROFILE)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "agsamkin.code")
@PropertySource(value = "classpath:/config/application.yml")
public class SpringConfigForIT {
    public static final String TEST_PROFILE = "test";
}
