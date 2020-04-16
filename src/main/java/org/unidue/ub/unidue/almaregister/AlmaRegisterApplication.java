package org.unidue.ub.unidue.almaregister;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.ajp.AbstractAjpProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class AlmaRegisterApplication {

    @Value("${tomcat.ajp.port:8009}")
    int ajpPort;

    @Value("${tomcat.ajp.secret:bad_wolf}")
    String ajpSecret;

    public static void main(String[] args) {
        SpringApplication.run(AlmaRegisterApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer() {
        return server -> {
            if (server != null) {
                server.addAdditionalTomcatConnectors(redirectConnector());
            }
        };
    }

    private Connector redirectConnector() {
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(ajpPort);
        ajpConnector.setSecure(false);
        ajpConnector.setAllowTrace(false);
        ajpConnector.setAttribute("tomcatAuthentication", false);
        ajpConnector.setAttribute("connectionTimeout", 20000);
        ajpConnector.setScheme("http");
        ajpConnector.setAttribute("packetSize", 65536);
        ((AbstractAjpProtocol) ajpConnector.getProtocolHandler()).setSecret(ajpSecret);
        return ajpConnector;
    }


}
