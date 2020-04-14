package org.unidue.ub.unidue.almaregister.client;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * configuration for the Feign clients to allow for basic authentication upon the requests to the settings backend
 * within the libintel architecture.
 */
public class FeignConfiguration {

    @Value("${alma.api.user.key}")
    private String almaUserApiKey;

    private static Logger log = LoggerFactory.getLogger(FeignConfiguration.class);

    @Bean
    public RequestInterceptor apiKeyInterceptor() {
        ApiKeyAuth apiKeyAuth = new ApiKeyAuth("query", "apikey");
        apiKeyAuth.setApiKey(almaUserApiKey);
        return apiKeyAuth;
    }
}
