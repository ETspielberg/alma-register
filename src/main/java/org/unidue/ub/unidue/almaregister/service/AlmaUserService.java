package org.unidue.ub.unidue.almaregister.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.AlmaUser;
import org.unidue.ub.unidue.almaregister.model.ContactInfo;
import org.unidue.ub.unidue.almaregister.model.Email;
import org.unidue.ub.unidue.almaregister.model.UserAccountType;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    @Value("${alma.source.institution.id}")
    private String almaInstitutionId;


    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
    }

    public AlmaUser generateAlmaUserFromShibbolethData() {
        String id = this.httpServletRequest.getHeader("AJP_uid");
        String email = this.httpServletRequest.getHeader("AJP_mail");
        String givenName = this.httpServletRequest.getHeader("AJP_givenName");
        String displayName = this.httpServletRequest.getHeader("AJP_displayName");
        String type = this.httpServletRequest.getHeader("AJP_eduScopedAffiliation");
        AlmaUser almaUser = new AlmaUser();
        almaUser.setFirstName(givenName);
        almaUser.setFullName(displayName);
        almaUser.setPrimaryId(id);
        Email emailData = new Email();
        emailData.setEmailAddress(email);
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(Collections.singletonList(emailData));
        almaUser.setContactInfo(contactInfo);
        UserAccountType userAccountType = new UserAccountType();
        if (type.contains("student"))
            userAccountType.setValue("student");
        else if (type.contains("staff"))
            userAccountType.setValue("staff");
        almaUser.setAccountType(userAccountType);
        return almaUser;
    }

    public AlmaUser createAlmaUser(AlmaUser almaUser) {
        return this.almaUserApiClient.postAlmawsV1Users(almaUser, "false", "", almaInstitutionId, almaUser.getPrimaryId());
    }
}
