package org.unidue.ub.unidue.almaregister.service;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.HisExport;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.repository.HisExportRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final HisExportRepository hisExportRepository;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    /**
     * constructor based autowiring of alma user api client, the servlet request and the hstudents data repository
     * @param almaUserApiClient the Feign client to the Alma User API
     * @param httpServletRequest the current request object
     * @param hisExportRepository the repository holding the students data
     */
    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest,
                    HisExportRepository hisExportRepository) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
        this.hisExportRepository = hisExportRepository;
    }

    /**
     * takes the Shibboleth attributes persistent-id, givenName, sn, affiliation, uid and mail and generates a
     * corresponding alma user registration request object.
     * @return an Alma user registration request object from which an Alma User object can be generated
     * @throws MissingShibbolethDataException thrown if the necessary Shibboleth attributes are not present.
     * @throws MissingHisDataException thrown if the data from the students information system are not present.
     */
    public RegistrationRequest generateRegistrationRequest() throws MissingShibbolethDataException, MissingHisDataException {
        RegistrationRequest registrationRequest = new RegistrationRequest();
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
        registrationRequest.firstName = ((String) this.httpServletRequest.getAttribute("SHIB_givenName"));
        registrationRequest.lastName = ((String) this.httpServletRequest.getAttribute("SHIB_sn"));
        String type = (String) this.httpServletRequest.getAttribute("SHIB_affiliation");
        String zimId = (String) this.httpServletRequest.getAttribute("SHIB_uid");
        registrationRequest.externalId = zimId;
        registrationRequest.email = (String) this.httpServletRequest.getAttribute("SHIB_mail");

        // if no data can be obtained from the shibboleth response
        if (type == null)
            throw new MissingShibbolethDataException("no type given");
        if (type.contains("student")) {
            registrationRequest.userStatus = "student";
            // if the user is a student collect the data from the student system to fill in further user information
            log.debug("setting attributes for student");
            List<HisExport> hisExportList = this.hisExportRepository.findAllByZimKennung(zimId);
            if (hisExportList == null || hisExportList.size() == 0)
                throw new MissingHisDataException("no HIS data for student with ZIM-ID " + zimId);
            HisExport hisExport = hisExportList.get(0);
            registrationRequest.primaryId = hisExport.getBibkz();
        } else if (type.contains("staff")){
            log.debug("setting attributes for staff member");
            registrationRequest.userStatus = "staff";
            // if the user is no student, data are only taken from the shibboleth response
            registrationRequest.primaryId = zimId;
        } else {
            log.debug("setting attributes for external user");
            registrationRequest.userStatus = "extern";
            // if the user is no student, data are only taken from the shibboleth response
            registrationRequest.primaryId = zimId;
        }
        return registrationRequest;
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

    /**
     * checks, whether a user with the given uid exists in Alma
     * @param identifier the uid to be checked
     * @return a AlmaUser object, if the uid was found in Alma, null otherwise.
     */
    public AlmaUser checkExistingUser(String identifier) {
        try {
            return this.almaUserApiClient.getUser(identifier, "application/json");
        } catch (FeignException fe) {
            return null;
        }
    }
}
