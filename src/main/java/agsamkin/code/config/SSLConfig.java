package agsamkin.code.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(value = "prod")
@Configuration
public class SSLConfig {
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() throws Exception {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        Ssl ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setCertificate("ssl/public.pem");
        ssl.setCertificatePrivateKey("ssl/private.key");
        ssl.setKeyStoreType("PKCS12");
        ssl.setKeyStorePassword("");

        factory.setSsl(ssl);
        factory.setPort(443);
        return factory;
    }
}
