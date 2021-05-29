package org.unidue.ub.unidue.almaregister.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.unidue.ub.unidue.almaregister.service.ShibbolethUserDetailsService;

import java.util.ArrayList;
import java.util.List;

/**
 * security configuration.
 */
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Security configuration which allows general access to all files except
     */
    @Configuration
    @Order(3)
    public static class PublicSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable().csrf().disable().authorizeRequests().anyRequest().permitAll().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }
    }


    /**
     * Security configuration for the access to the api-key protected endpoint. The order ensures that in case of
     * api-key provided allowed access this config is evaluated first.
     * see from https://stackoverflow.com/questions/48446708/securing-spring-boot-api-with-api-key-and-secret for details
     */
    @Configuration
    @Order(2)
    public static class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Value("${libintel.alma.register.header.name}")
        private String headerName;

        @Value("${libintel.alma.register.header.value}")
        private String headerValue;

        @Override
        protected void configure(HttpSecurity httpSecurity) throws Exception {
            APIKeyAuthFilter filter = new APIKeyAuthFilter(headerName);
            filter.setAuthenticationManager(authentication -> {
                String principal = (String) authentication.getPrincipal();
                if (!headerValue.equals(principal))
                {
                    throw new BadCredentialsException("The API key was not found or not the expected value.");
                }
                authentication.setAuthenticated(true);
                return authentication;
            });
            httpSecurity.
                    antMatcher("/api/**").
                    csrf().disable().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                    and().addFilter(filter).authorizeRequests().anyRequest().access( "hasIpAddress('95.172.90.160/27') or hasIpAddress('31.186.254.128/28') or hasIpAddress('216.147.214.128/26') or hasIpAddress('216.147.218.128/26') or hasIpAddress('216.147.218.64/26') or hasIpAddress('216.147.215.0/27') or hasIpAddress('192.170.176.1')");
        }
    }


    /**
     * Security configuration for the access to the shibboleth protected endpoint. The order ensures that in case of
     * shibboleth allowed access this config is evaluated first. The user details are taken from the shibboleth response.
     */
    @Configuration
    @Order(1)
    public static class SecuredSecurityConfiguration extends WebSecurityConfigurerAdapter {

        private final ShibbolethUserDetailsService shibbolethUserDetailsService;


        /**
         * constructor based autowiring
         *
         * @param shibbolethUserDetailsService retrieves the user details from the parameters set by the shibboleth service provider
         */
        SecuredSecurityConfiguration(ShibbolethUserDetailsService shibbolethUserDetailsService) {
            this.shibbolethUserDetailsService = shibbolethUserDetailsService;
        }


        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/secure/**").httpBasic().disable()
                    .authorizeRequests().and()
                    .addFilterAfter(shibbolethFilter(), RequestHeaderAuthenticationFilter.class).authorizeRequests()
                    .antMatchers("/secure/**").authenticated()
                    .and().csrf().disable();
        }

        @Bean(name = "shibbolethFilter")
        public RequestHeaderAuthenticationFilter shibbolethFilter() {
            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader("Shib-Session-ID");
            requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
            return requestHeaderAuthenticationFilter;
        }

        @Bean
        @Override
        protected AuthenticationManager authenticationManager() {
            final List<AuthenticationProvider> providers = new ArrayList<>(1);
            providers.add(preauthAuthProvider());
            return new ProviderManager(providers);
        }

        @Bean(name = "preAuthProvider")
        PreAuthenticatedAuthenticationProvider preauthAuthProvider() {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(userDetailsServiceWrapper());
            return provider;
        }

        @Bean
        UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> userDetailsServiceWrapper() {
            UserDetailsByNameServiceWrapper<PreAuthenticatedAuthenticationToken> wrapper = new UserDetailsByNameServiceWrapper<>();
            wrapper.setUserDetailsService(shibbolethUserDetailsService);
            return wrapper;
        }
    }

}
