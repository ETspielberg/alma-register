package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.AlmaUser;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogService {

    private final Logger log = LoggerFactory.getLogger(LogService.class);

    public void logSuccess(AlmaUser almaUser, HttpServletRequest httpServletRequest) {
        log.info(String.format("'User %s %s successfully registered' | primaryId: '%s', userGroup: '%s', remoteAddress: %s, userAgent: '%s', error: false, errorMessage: ''",
                almaUser.getFirstName(),
                almaUser.getLastName(),
                almaUser.getPrimaryId(),
                almaUser.getUserGroup().getValue(),
                getRemoteAddress(httpServletRequest),
                getUserAgent(httpServletRequest)));
    }

    public void logError(RegistrationRequest registrationRequest, HttpServletRequest httpServletRequest, Exception exception) {
        log.error(String.format("'An error occurred upon registering %s %s' | primaryId: '%s', userGroup: '%s', remoteAddress: %s, userAgent: '%s', error: true, errorMessage: '%s'",
                registrationRequest.getFirstName(),
                registrationRequest.getLastName(),
                registrationRequest.getPrimaryId(),
                registrationRequest.getUserStatus(),
                getRemoteAddress(httpServletRequest),
                getUserAgent(httpServletRequest),
                exception.getMessage()));
    }

    private String getUserAgent(HttpServletRequest httpServletRequest) {
        String userAgent;
        try {
            userAgent = httpServletRequest.getHeader("User-Agent");
            if (userAgent == null)
                userAgent = "n.a.";
        } catch (Exception e) {
            userAgent = "n.a.";
        }
        return userAgent;
    }

    private String getRemoteAddress(HttpServletRequest httpServletRequest) {
        String remoteAddress;
        try {
            remoteAddress = httpServletRequest.getRemoteAddr();
            if (remoteAddress == null)
                remoteAddress = "n.a.";
        } catch (Exception e) {
            remoteAddress = "n.a.";
        }
        return remoteAddress;
    }

}
