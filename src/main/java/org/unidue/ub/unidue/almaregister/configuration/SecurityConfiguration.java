package org.unidue.ub.unidue.almaregister.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
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

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Security configuration which allows general access to all files except
     */
    @Configuration
    public static class PublicSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable().csrf().disable().authorizeRequests().anyRequest().permitAll().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        }
    }


    /**
     * Security configuration for the access to the shibboleth protected endpoint. The order ensures that in case of
     * shibboleth allowed access this config is evaluated first. The user details are taken from the shiboleth response.
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
