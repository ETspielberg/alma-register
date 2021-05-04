package org.unidue.ub.unidue.almaregister.configuration;

import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;

@Configuration
public class AddressWsClientConfig {

    @Value("${libintel.address.ws.url}")
    private String addressWebServiceUrl;

    @Value("${libintel.address.ws.username}")
    private String addressWebServiceUsername;

    @Value("${libintel.address.ws.password}")
    private String addressWebServicePassword;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("org.unidue.ub.unidue.almaregister.model.wsclient");
        return marshaller;
    }
    @Bean
    public AddressWebServiceClient addressWebServiceClient(Jaxb2Marshaller marshaller) {
        AddressWebServiceClient client = new AddressWebServiceClient();
        client.setDefaultUri(addressWebServiceUrl);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        client.setInterceptors(new ClientInterceptor[]{ securityInterceptor() });
        return client;
    }

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor() {
        Wss4jSecurityInterceptor security = new Wss4jSecurityInterceptor();

        // Adds "Timestamp" and "UsernameToken" sections in SOAP header
        security.setSecurementActions(WSHandlerConstants.TIMESTAMP + " " + WSHandlerConstants.USERNAME_TOKEN);

        // Set values for "UsernameToken" sections in SOAP header
        security.setSecurementPasswordType(WSConstants.PW_TEXT);
        security.setSecurementUsername(addressWebServiceUsername);
        security.setSecurementPassword(addressWebServicePassword);
        return security;
    }
}
