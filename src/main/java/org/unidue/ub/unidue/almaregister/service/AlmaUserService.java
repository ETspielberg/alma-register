package org.unidue.ub.unidue.almaregister.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.AlmaUserRequest;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final HisExportRepository hisExportRepository;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest,
                    HisExportRepository hisExportRepository) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
        this.hisExportRepository = hisExportRepository;
    }

    /**
     * creates a Alma user request from the shibboleth attributes and the stored information of the HIS system
     * @return an Alma user request
     * @throws MissingShibbolethDataException thrown if the necessary Shibboleth attributes are not present in the request
     * qthrows MissingHisDataException thrown if no data can be found in HIS export for a given ZIM ID
     */
    public AlmaUserRequest generateAlmaUserRequestFromShibbolethData() throws MissingShibbolethDataException, MissingHisDataException {
        AlmaUser almaUser = new AlmaUser();
        try {
            // try to read the shibboleth id
            String id = ((String) this.httpServletRequest.getAttribute("SHIB_persistent-id")).split("!")[2];
            log.debug("trying to create alma account for user with id " + id);
        } catch (Exception e) {
            // if no shibboleth id is found throw a MissingShibbolethDataException
            log.error("failed to create account", e);
            throw new MissingShibbolethDataException("no shibboleth attributes in request");
        }

        // set the values as obtained by the shibboleth attributes
        almaUser.setFirstName((String) this.httpServletRequest.getAttribute("SHIB_givenName"));
        almaUser.setLastName((String) this.httpServletRequest.getAttribute("SHIB_sn"));
        String type = (String) this.httpServletRequest.getAttribute("SHIB_affiliation");
        String zimId = (String) this.httpServletRequest.getAttribute("uid");

        // create email data
        Email emailData = new Email();
        EmailEmailType emailEmailType = new EmailEmailType();
        emailData.addEmailTypeItem(emailEmailType);
        emailData.setEmailAddress((String) this.httpServletRequest.getAttribute("SHIB_mail"));

        // if no data can be obtained from the shibboleth response
        if (type == null)
            throw new MissingShibbolethDataException("no type given");
        if (type.contains("student")) {
            // if the user is a student collect the data from the student system to fill in further user information
            log.debug("setting attributes for student");
            List<HisExport> hisExportList = this.hisExportRepository.findAllByZimKennung(zimId);
            if (hisExportList == null || hisExportList.size() == 0)
                throw new MissingHisDataException("no HIS data for student with ZIM-ID " + zimId);
            HisExport hisExport = hisExportList.get(0);
            almaUser.setPrimaryId(hisExport.getBibkz());
            almaUser.setExternalId(zimId);

            // set email properties to private as students account
            emailEmailType.setValue("private");
            emailEmailType.setDesc("Private Mail");
        } else {
            // if the user is no student, data are only taken from the shibboleth resposne
            log.debug("setting attributes for staff member");
            almaUser.setPrimaryId(zimId);
            almaUser.setExternalId(zimId);

            // set email properties to work as obtained from shibboleth data
            emailEmailType.setValue("work");
            emailEmailType.setDesc("Work Mail");
        }

        // create the contact information and add them to the alma user
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmail(Collections.singletonList(emailData));
        almaUser.setContactInfo(contactInfo);

        // set the account type
        UserAccountType userAccountType = new UserAccountType();
        userAccountType.setValue("EXTERNAL");

        return new AlmaUserRequest().withAlmaUser(almaUser);
    }

    /**
     * calls the Alma API to create a user account
     * @param almaUser the AlmaUser to be created
     * @param pinNotification whether a notification E-Mail about the new pin shall be send
     * @throws AlmaConnectionException thrown, ich the connection to the Alma API cannot be established
     */
    public void createAlmaUser(AlmaUser almaUser, boolean pinNotification) throws AlmaConnectionException {
        try {
            this.almaUserApiClient.postUsers("application/json", almaUser, pinNotification);
        } catch (Exception e) {
            log.warn("could not create user", e);
            throw new AlmaConnectionException("could not create user");
        }
    }
}
