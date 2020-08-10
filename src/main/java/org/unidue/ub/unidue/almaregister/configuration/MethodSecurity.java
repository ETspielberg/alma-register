package org.unidue.ub.unidue.almaregister.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * enables the method security
 */
@Configuration
@EnableGlobalMethodSecurity(
        securedEnabled = true)
public class MethodSecurity extends GlobalMethodSecurityConfiguration {
}
