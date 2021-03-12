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
import org.unidue.ub.unidue.almaregister.model.Overdue;
import org.unidue.ub.unidue.almaregister.model.OverdueReport;
import org.unidue.ub.unidue.almaregister.model.RegistrationRequest;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;
import org.unidue.ub.unidue.almaregister.service.exceptions.AlmaConnectionException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingHisDataException;
import org.unidue.ub.unidue.almaregister.service.exceptions.MissingShibbolethDataException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AlmaUserService {

    private final AlmaUserApiClient almaUserApiClient;

    private final HttpServletRequest httpServletRequest;

    private final AddressWebServiceClient addressWebServiceClient;

    private final AlmaAnalyticsReportClient almaAnalyticsReportClient;

    private final static Logger log = LoggerFactory.getLogger(AlmaUserService.class);

    /**
     * constructor based autowiring of alma user api client, the servlet request and the hstudents data repository
     *
     * @param almaUserApiClient       the Feign client to the Alma User API
     * @param httpServletRequest      the current request object
     * @param addressWebServiceClient the repository holding the students data
     */
    AlmaUserService(AlmaUserApiClient almaUserApiClient,
                    HttpServletRequest httpServletRequest,
                    AddressWebServiceClient addressWebServiceClient,
                    AlmaAnalyticsReportClient almaAnalyticsReportClient) {
        this.almaUserApiClient = almaUserApiClient;
        this.httpServletRequest = httpServletRequest;
        this.addressWebServiceClient = addressWebServiceClient;
        this.almaAnalyticsReportClient = almaAnalyticsReportClient;
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
            registrationRequest.primaryId = zimId;
            // if the user is a student collect the data from the student system to fill in further user information
            log.debug("setting attributes for student");
            try {
                String matrikelString = ((String) this.httpServletRequest.getAttribute("SHIB_schacPersonalUniqueCode"));
                log.info("retrieved matrikel number " + matrikelString);
                long matrikel = Long.getLong(matrikelString);
                ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddressByMatrikel(matrikel);
                switch (String.valueOf(response.getAddress().getGenderId())) {
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
                log.warn("an error occurred: could not extract matrikel number");
                return registrationRequest;
            }
        } else if (type.contains("staff")) {
            log.debug("setting attributes for staff member");
            registrationRequest.userStatus = "06";
            // if the user is no student, data are only taken from the shibboleth response
            registrationRequest.primaryId = zimId;
        } else {
            log.debug("setting attributes for external user");
            registrationRequest.userStatus = "22";
            // if the user is no student, data are only taken from the shibboleth response
            registrationRequest.primaryId = zimId;
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

    @Async("threadPoolTaskExecutor")
    @Scheduled(cron = "0 0 7 * * *")
    public void updateUserAdresses() {
        Set<String> primaryIds = new HashSet<>();
        try {
            List<Overdue> reportResults = this.almaAnalyticsReportClient.getReport(Overdue.PATH, OverdueReport.class).getRows();
            for (Overdue overdue : reportResults) {
                primaryIds.add(overdue.getPrimaryIdentifier());
                log.info(overdue.getPrimaryIdentifier());
            }
            log.info(String.valueOf(primaryIds.size()));
            primaryIds.forEach(this::extendUser);

        } catch (IOException ioe) {
            log.error("could not retrieve analytics report :", ioe);
        }
    }

    private void extendUser(String primaryId) {
        AlmaUser user = this.almaUserApiClient.getUser(primaryId, "application/json");
        long userNumber = 0L;
        if (!"01".equals(user.getUserGroup().getValue())) {
            log.warn(String.format("could not update addresse for user %s: not a student", primaryId));
            return;
        }
        for (UserIdentifier userIdentifier : user.getUserIdentifier())
            if ("02".equals(userIdentifier.getIdType().getValue()))
                userNumber = Long.parseLong(userIdentifier.getValue());
        log.info(String.format("updating user with id %d", userNumber));
        if (userNumber != 0L) {
            ReadAddressByRegistrationnumberResponse response = this.addressWebServiceClient.getAddressByMatrikel(userNumber);
            if (response != null) {
                Address address = new Address().addAddressTypeItem(new AddressAddressType().value("home"))
                        .city(response.getAddress().getCity())
                        .country(new AddressCountry().value(response.getAddress().getCountry()))
                        .line1(response.getAddress().getStreet())
                        .line2(response.getAddress().getAddressaddition())
                        .line3(response.getAddress().getPostcode() + " " + response.getAddress().getCity());
                user.getContactInfo().addAddressItem(address);
                this.almaUserApiClient.updateUser(user.getPrimaryId(), user);
                log.info(String.format("retrieved address for user %s", primaryId ));
            } else
                log.warn(String.format("could not get address for user %s from his system.", primaryId));
        } else
            log.warn(String.format("could not update addresse for user %s: no matrikel number", primaryId));
    }
}
