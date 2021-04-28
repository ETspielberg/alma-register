package org.unidue.ub.unidue.almaregister.service;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.client.AlmaAnalyticsReportClient;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.his.HisExport;
import org.unidue.ub.unidue.almaregister.model.Overdue;
import org.unidue.ub.unidue.almaregister.model.OverdueReport;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.exceptions.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingHisDataException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingShibbolethDataException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final HisService hisService;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    /**
     * constructor based autowiring of alma user api client, the servlet request and the hstudents data repository
     *
     * @param almaUserApiClient       the Feign client to the Alma User API
     * @param httpServletRequest      the current request object
     */
    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest,
                    HisService hisService) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
        this.hisService = hisService;
    }

    /**
     * takes the Shibboleth attributes persistent-id, givenName, sn, affiliation, uid and mail and generates a
     * corresponding alma user registration request object.
     *
     * @return an Alma user registration request object from which an Alma User object can be generated
     * @throws MissingShibbolethDataException thrown if the necessary Shibboleth attributes are not present.
     * @throws MissingHisDataException        thrown if the data from the students information system are not present.
     */
    public RegistrationRequest generateRegistrationRequest() throws MissingShibbolethDataException, MissingHisDataException {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        try {
            this.httpServletRequest.setCharacterEncoding(StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException uee) {
            log.warn("could not retrieve request with encoding utf-8. using standard encoding instead");
        }
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
        registrationRequest.primaryId = zimId;
        // if no data can be obtained from the shibboleth response
        if (type == null)
            throw new MissingShibbolethDataException("no type given");
        if (type.contains("student")) {
            registrationRequest.userStatus = "01";
            // if the user is a student collect the data from the student system to fill in further user information
            log.debug("setting attributes for student");
            try {
                HisExport hisExports = this.hisService.getByZimId(zimId);
                String matrikelString = hisExports.getBibkz();
                if (!hisExports.getEmail().equals(registrationRequest.email))
                    registrationRequest.additionalEmailAdresses.add(hisExports.getEmail());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                try {
                    LocalDate birthday = LocalDate.parse(hisExports.getGebdat(), formatter);
                    registrationRequest.setBirthDate(birthday);
                } catch (Exception e) {
                    log.warn("could not parse birthday",e);
                }
                registrationRequest.cardNumber = matrikelString;
                long matrikel = Long.parseLong(hisExports.getMtknr());
                long cardCurrens = 0L;
                if (hisExports.getCardCurrens() != null && !hisExports.getCardCurrens().isEmpty()) {
                    cardCurrens = Long.parseLong(hisExports.getCardCurrens());
                }
                registrationRequest.cardNumber = String.format("%sS%08d%02d", hisExports.getCampus(), matrikel, cardCurrens);
                switch (String.valueOf(hisExports.getGeschl())) {
                    case "0": {
                        registrationRequest.setGender("NONE");
                        break;
                    }
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
                }
            } catch (Exception e) {
                log.warn("an error occurred: could not extract matrikel number", e);
                return registrationRequest;
            }
        } else if (type.contains("staff")) {
            log.debug("setting attributes for staff member");
            registrationRequest.userStatus = "06";
        } else {
            log.debug("setting attributes for external user");
            registrationRequest.userStatus = "22";
        }
        return registrationRequest;
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
            return this.almaUserApiClient.getUser(identifier, "application/json");
        } catch (FeignException fe) {
            return null;
        }
    }


    public AlmaUser getExistingAccount(String cardNumber) {
        return this.almaUserApiClient.getUser(cardNumber, "application/json");
    }

    public AlmaUser updateAlmaUser(String userId, AlmaUser almaUser) {
        return this.almaUserApiClient.updateUser(userId, almaUser);
    }

    public boolean existsByLastnameAndBirthday(RegistrationRequest registrationRequest) {
        String searchstring = String.format("q=last_name~%s", registrationRequest.lastName);
        int limit = 50;
        int offset = 0;
        AlmaUsers almaUsers = this.almaUserApiClient.retrieveAlmaUsers("application/json", searchstring, limit,offset);
        List<AlmaUser> usersFound = new ArrayList<>(almaUsers.getUsers());
        while (almaUsers.getTotalRecordCount() < usersFound.size()) {
            offset += limit;
            usersFound.addAll(this.almaUserApiClient.retrieveAlmaUsers("application/json", searchstring, limit,offset).getUsers());
        }
        for (AlmaUser almaUser : usersFound) {
            if (almaUser.getBirthDate().compareTo(dateFromLocalDate(registrationRequest.birthDate)) == 0)
                return true;
        }
        return false;
    }

    private Date dateFromLocalDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.of("GMT");
        return Date.from(localDate.atStartOfDay().atZone(defaultZoneId).toInstant());
    }
}
