package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.AlmaUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
    }

    public AlmaUserRequest generateAlmaUserRequestFromShibbolethData() throws MissingShibbolethDataException {
        String id = (String) this.httpServletRequest.getHeader("persistent-id");
        String email = (String) this.httpServletRequest.getHeader("mail");
        String givenName = (String) this.httpServletRequest.getHeader("givenName");
        String surname = (String) this.httpServletRequest.getHeader("sn");
        String type = (String) this.httpServletRequest.getHeader("affiliation");
        if (type == null)
            throw new MissingShibbolethDataException("no type given");
        return new AlmaUserRequest()
                .withAffiliation(type)
                .withEmail(email)
                .withInternalId(id)
                .withFirstName(givenName)
                .withSurname(surname);
    }

    public AlmaUser generateFromAlmaUserRequest(AlmaUserRequest almaUserRequest) {
        String type = almaUserRequest.getAffiliation();

        AlmaUser almaUser = new AlmaUser();
        almaUser.setFirstName(almaUserRequest.getFirstName());
        almaUser.setLastName(almaUserRequest.getSurname());
        almaUser.setPrimaryId(almaUserRequest.getInternalId());
        almaUser.setExternalId(almaUserRequest.getInternalId());
        Email emailData = new Email();
        EmailEmailType emailEmailType = new EmailEmailType();
        if (type.contains("staff")) {
            emailEmailType.setValue("work");
            emailEmailType.setDesc("Work Mail");
        } else {
            emailEmailType.setValue("private");
            emailEmailType.setDesc("Private Mail");
        }
        emailData.addEmailTypeItem(emailEmailType);
        emailData.setEmailAddress(almaUserRequest.getEmail());
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(Collections.singletonList(emailData));
        almaUser.setContactInfo(contactInfo);
        UserAccountType userAccountType = new UserAccountType();

        if (type.contains("student"))
            userAccountType.setValue("EXTERNAL");
        else if (type.contains("staff"))
            userAccountType.setValue("EXTERNAL");
        almaUser.setAccountType(userAccountType);
        return almaUser;
    }

    public AlmaUser createAlmaUser(AlmaUser almaUser) {
        return this.almaUserApiClient.postAlmawsV1Users("application/json", almaUser);
    }
}
