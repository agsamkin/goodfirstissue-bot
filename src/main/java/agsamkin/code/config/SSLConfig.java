package agsamkin.code.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLConfig {
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() throws Exception {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        Ssl ssl = new Ssl();
        ssl.setEnabled(true);
        ssl.setCertificate("YOURPUBLIC.pem");
        ssl.setCertificatePrivateKey("YOURPRIVATE.key");
        ssl.setKeyStoreType("PKCS12");
        ssl.setKeyStorePassword(""); // without this decrytption fails
        factory.setSsl(ssl);
        factory.setPort(443);

        return factory;
    }
}