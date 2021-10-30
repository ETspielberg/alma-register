package org.unidue.ub.unidue.almaregister.service;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.service.exceptions.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingHisDataException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingShibbolethDataException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final HisService hisService;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    /**
     * constructor based autowiring of alma user api client, the servlet request and the hstudents data repository
     *
     * @param almaUserApiClient  the Feign client to the Alma User API
     * @param httpServletRequest the current request object
     */
    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest,
                    HisService hisService) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
        this.hisService = hisService;
    }

    /**
     * retrieves an existing Alma account
     * @param identifier the identifier by which the Alma account is retrieved
     * @return the created AlmaUser object
     */
    public AlmaUser getExistingAccount(String identifier) {
        return this.almaUserApiClient.getUser(identifier, "application/json");
    }

    /**
     * updates an existing Alma account
     * @param identifier the identifier of the Alma account to be updated
     * @param almaUser the Alma account object to be updated
     * @return the updated AlmaUser object
     */
    public AlmaUser updateAlmaUser(String identifier, AlmaUser almaUser) {
        return this.almaUserApiClient.updateUser(identifier, almaUser);
    }

    /**
     * calls the Alma API to create a user account
     *
     * @param almaUser        the AlmaUser to be created
     * @param pinNotification whether a notification E-Mail about the new pin shall be send
     * @throws AlmaConnectionException thrown, ich the connection to the Alma API cannot be established
     */
    public AlmaUser createAlmaUser(AlmaUser almaUser, boolean pinNotification) throws AlmaConnectionException {
        try {
            return this.almaUserApiClient.postUsers("application/json", almaUser, pinNotification);
        } catch (Exception e) {
            log.warn("could not create user", e);
            throw new AlmaConnectionException("could not create user");
        }
    }

    /**
     * checks, whether a user with the given uid exists in Alma
     *
     * @param identifier the uid to be checked
     * @return a AlmaUser object, if the uid was found in Alma, null otherwise.
     */
    public AlmaUser checkExistingUser(String identifier) {
        try {
            return getExistingAccount(identifier);
        } catch (FeignException fe) {
            return null;
        }
    }

    /**
     * checks, whether a user with the given uid exists in Alma
     *
     * @param registrationRequest the registration request to be checked
     * @return a AlmaUser object, if the uid was found in Alma, null otherwise.
     */
    public boolean userExists(RegistrationRequest registrationRequest) {
        if (!registrationRequest.primaryId.isEmpty() && checkExistingUser(registrationRequest.primaryId) != null) {
            registrationRequest.setExists(true);
            registrationRequest.setDuplicateId(registrationRequest.primaryId);
            registrationRequest.setDuplicateIdType("UniKennung");
            log.info("user duplicate found by UniKennung");
            return true;
        }
        if (!registrationRequest.cardNumber.isEmpty() && checkExistingUser(registrationRequest.cardNumber) != null) {
            registrationRequest.setExists(true);
            registrationRequest.setDuplicateId(registrationRequest.cardNumber);
            registrationRequest.setDuplicateIdType("Strichcode");
            log.info("user duplicate found by Strichcode");
            return true;
        }
        if (registrationRequest.calculateHash() != null && checkExistingUser(registrationRequest.calculateHash()) != null) {
            registrationRequest.setExists(true);
            registrationRequest.setDuplicateId(registrationRequest.calculateHash());
            registrationRequest.setDuplicateIdType("Dublettencheck");
            log.info("user duplicate found by Dublettencheck");
            return true;
        }
        else
            return false;
    }

    /**
     * takes the Shibboleth attributes persistent-id, givenName, sn, affiliation, uid and mail and generates a
     * corresponding alma user registration request object.
     *
     * @return an Alma user registration request object from which an Alma User object can be generated
     * @throws MissingShibbolethDataException thrown if the necessary Shibboleth attributes are not present.
     * @throws MissingHisDataException        thrown if the data from the students information system are not present.
     */
    public RegistrationRequest generateRegistrationRequestFromShibboleth() throws MissingShibbolethDataException, MissingHisDataException {
        // create bare registration request
        RegistrationRequest registrationRequest = new RegistrationRequest();

        // set encoding of the servlet request in order to retrieve apropriate characters
        try {
            this.httpServletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException uee) {
            log.warn("could not retrieve request with encoding utf-8. using standard encoding instead");
        }

        // retrieve id from shibboleth attributes
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
        registrationRequest.firstName = coreectEncoding(((String) this.httpServletRequest.getAttribute("SHIB_givenName")));
        registrationRequest.lastName = coreectEncoding(((String) this.httpServletRequest.getAttribute("SHIB_sn")));
        String type = (String) this.httpServletRequest.getAttribute("SHIB_affiliation");
        String zimId = (String) this.httpServletRequest.getAttribute("SHIB_uid");
        if (zimId == null) {
            String eppn = (String) this.httpServletRequest.getAttribute("SHIB_eppn");
            if (eppn != null && eppn.contains("@"))
                zimId = eppn.split("@")[0];
        }
        registrationRequest.externalId = zimId;
        registrationRequest.email = coreectEncoding((String) this.httpServletRequest.getAttribute("SHIB_mail"));
        registrationRequest.primaryId = zimId;

        // first check: if type is set and has the staff options, set the account as staff
        if (type != null && type.contains("staff")) {
            log.debug("setting attributes for staff member");
            registrationRequest.userStatus = "06";
        } else {
            //if it is not a staff account, retrieve the student data
            HisExport hisExport = this.hisService.getByZimId(zimId);
            // if there are student data, set the account type to student and add the student data
            if (hisExport != null) {
                registrationRequest.userStatus = "01";
                addStudentData(registrationRequest, hisExport);
                log.debug("adding his data for student");
            } else {
                // if there are no sudent data, but the type is set as student, set the account type to student
                if (type != null && type.contains("student")) {
                    log.debug("setting attributes for student");
                    registrationRequest.userStatus = "01";
                } else {
                    // if there are no student data and if there is no student type, set the account to external
                    log.debug("setting attributes for external");
                    registrationRequest.userStatus = "22";
                }
            }
        }
        return registrationRequest;
    }

    private String coreectEncoding(String string) {
        Charset fromCharset = StandardCharsets.ISO_8859_1;
        Charset toCharset = StandardCharsets.UTF_8;
        return new String(string.getBytes(fromCharset), toCharset);
    }

    private void addStudentData(RegistrationRequest registrationRequest, HisExport hisExport) {
        try {

            if (!hisExport.getEmail().equals(registrationRequest.email))
                registrationRequest.additionalEmailAdresses.add(hisExport.getEmail());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                LocalDate birthday = LocalDate.parse(hisExport.getGebdat(), formatter);
                registrationRequest.setBirthDate(birthday);
            } catch (Exception e) {
                log.warn("could not parse birthday", e);
            }

            // calculate card number from his export data
            long matrikel = Long.parseLong(hisExport.getMtknr());
            registrationRequest.matrikelNumber = hisExport.getMtknr();
            long cardCurrens = 0L;
            if (hisExport.getCardCurrens() != null && !hisExport.getCardCurrens().isEmpty()) {
                cardCurrens = Long.parseLong(hisExport.getCardCurrens());
            }
            registrationRequest.cardNumber = String.format("%sS%08d%02d", hisExport.getCampus(), matrikel, cardCurrens);

            // set gender
            switch (String.valueOf(hisExport.getGeschl())) {
                case "1": {
                    registrationRequest.setGender("MALE");
                    break;
                }
                case "2": {
                    registrationRequest.setGender("FEMALE");
                    break;
                }
                case "3": {
                    registrationRequest.setGender("OTHER");
                    break;
                }
                default: {
                    registrationRequest.setGender("NONE");
                    break;
                }
            }
        } catch (Exception e) {
            log.warn("an error occurred: could not extract matrikel number", e);
        }
    }
}
