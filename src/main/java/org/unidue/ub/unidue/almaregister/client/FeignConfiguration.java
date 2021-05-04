package org.unidue.ub.unidue.almaregister.client;

import feign.RequestInterceptor;
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

    /**
     * appropriate request interceptor to add the authentication information.
     * @return the request interceptor
     */
    @Bean
    public RequestInterceptor apiKeyInterceptor() {
        // add authentication information as query parameter 'apikey'
        ApiKeyAuth apiKeyAuth = new ApiKeyAuth("query", "apikey");

        //set the api key as defied in the properties
        apiKeyAuth.setApiKey(almaUserApiKey);
        return apiKeyAuth;
    }

    /**
     * enabling Feign Logging
     * @return Feign logger Level
     */
    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
}
