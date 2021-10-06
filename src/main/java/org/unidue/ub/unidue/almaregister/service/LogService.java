package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.AlmaUser;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogService {

    private final Logger log = LoggerFactory.getLogger(LogService.class);

    public void logSuccess(AlmaUser almaUser, HttpServletRequest httpServletRequest) {
        String userAgent;
        String remoteAddress;
        try {
            remoteAddress = httpServletRequest.getRemoteAddr();
            if (remoteAddress == null)
                remoteAddress = "n.a.";
        } catch (Exception e) {
            remoteAddress = "n.a.";
        }
        try {
            userAgent = httpServletRequest.getHeader("User-Agent");
            if (userAgent == null)
                userAgent = "n.a.";
        } catch (Exception e) {
            userAgent = "n.a.";
        }
        log.info(String.format("User '%s %s' successfully registered | primaryId: %s, userGroup: %s, remoteAddress: %s, userAgent; %s",
                almaUser.getFirstName(),
                almaUser.getLastName(),
                almaUser.getPrimaryId(),
                almaUser.getUserGroup().getValue(),
                remoteAddress,
                userAgent));
    }

}
