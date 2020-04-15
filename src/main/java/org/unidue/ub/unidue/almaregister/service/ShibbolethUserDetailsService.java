package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Service
public class ShibbolethUserDetailsService implements UserDetailsService {

    private final HttpServletRequest httpServletRequest;


    /**
     * constructor based autowiring of the active http request object
     * @param httpServletRequest the servlet request objct
     */
    public ShibbolethUserDetailsService(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * retrieves the user from the http request header and populates a userdetails objet with the roles as determined from the scoped affiliation.
     * @param username the username as determined by the appropriate Shibboleth-AJP-header
     * @return a userdetails object
     * @throws UsernameNotFoundException a standard exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String scopedAffiliation = this.httpServletRequest.getHeader("AJP_eduScopedAffiliation");
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        if (scopedAffiliation != null)
                grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + scopedAffiliation.substring(0, scopedAffiliation.indexOf("@")).toUpperCase()));
        UserDetails user = new User(username, "password", true, true, true, true,
                grantedAuthoritySet);
        return user;
    }
}
