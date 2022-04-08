package org.unidue.ub.unidue.almaregister.service;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.unidue.ub.alma.shared.user.*;
import org.unidue.ub.unidue.almaregister.client.AddressWebServiceClient;
import org.unidue.ub.unidue.almaregister.client.AlmaAnalyticsReportClient;
import org.unidue.ub.unidue.almaregister.client.AlmaUserApiClient;
import org.unidue.ub.unidue.almaregister.model.reports.Overdue;
import org.unidue.ub.unidue.almaregister.model.reports.OverdueReport;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByAccountResponse;
import org.unidue.ub.unidue.almaregister.model.wsclient.ReadAddressByRegistrationnumberResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ScheduledService {

    private final JobLauncher jobLauncher;

    private final Job hisJob;

    private final AlmaAnalyticsReportClient almaAnalyticsReportClient;

    private final AlmaUserApiClient almaUserApiClient;

    private final AddressWebServiceClient addressWebServiceClient;

    private final Logger log = LoggerFactory.getLogger(ScheduledService.class);

    ScheduledService(JobLauncher jobLauncher,
                     Job hisJob,
                     AlmaAnalyticsReportClient almaAnalyticsReportClient,
                     AlmaUserApiClient almaUserApiClient,
                     AddressWebServiceClient addressWebServiceClient) {
        this.jobLauncher = jobLauncher;
        this.hisJob = hisJob;
        this.almaAnalyticsReportClient = almaAnalyticsReportClient;
        this.almaUserApiClient = almaUserApiClient;
        this.addressWebServiceClient = addressWebServiceClient;
    }

    @Scheduled(cron = "0 0 23 * * *")
    @Async("threadPoolTaskExecutor")
    public void runImportJob() throws Exception {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek() == DayOfWeek.SUNDAY)
            today = today.minusDays(2);
        else if (today.getDayOfWeek() == DayOfWeek.SATURDAY)
            today = today.minusDays(1);
        String filename = today.format(formatter) + "-1";
        jobParametersBuilder.addString("his.filename", filename).toJobParameters();
        jobParametersBuilder.addDate("date", new Date()).toJobParameters();
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        jobLauncher.run(hisJob, jobParameters);
    }

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
        AlmaUser user;
        try {
            user = this.almaUserApiClient.getUser(primaryId, "application/json");
        } catch (FeignException fe) {
            log.warn("could not retrieve user with id " + primaryId, fe);
            return;
        }
        long userNumber = 0L;
        String zimId = "";
        /*
        if (!"01".equals(user.getUserGroup().getValue()) || !"01".equals(user.getUserGroup().getValue()) || !"01".equals(user.getUserGroup().getValue())) {
            log.warn(String.format("could not update address for user %s: not a student", primaryId));
            return;
        }
         */
        for (UserIdentifier userIdentifier : user.getUserIdentifier()) {
            if ("01".equals(userIdentifier.getIdType().getValue())) {
                String barcode = userIdentifier.getValue().toLowerCase(Locale.ROOT);
                if (barcode.startsWith("es"))
                    barcode = barcode.replace("es", "");
                else if (barcode.startsWith("ds"))
                    barcode = barcode.replace("ds", "");
                else
                    continue;
                barcode = barcode.substring(0, barcode.length() - 2);
                userNumber = Long.parseLong(barcode);
            } else if ("02".equals(userIdentifier.getIdType().getValue()))
                userNumber = Long.parseLong(userIdentifier.getValue());
            else if ("03".equals(userIdentifier.getIdType().getValue()))
                zimId = userIdentifier.getValue();
        }
        log.info(String.format("updating user with id %d and zim id %s", userNumber, zimId));
        if (!zimId.isEmpty()) {
            ReadAddressByAccountResponse zimIdResponse = this.addressWebServiceClient.getAddressByZimId(zimId);
            if (zimIdResponse != null) {
                Address address = new Address().preferred(true)
                        .addAddressTypeItem(new AddressAddressType().value("home"))
                        .city(zimIdResponse.getAddress().getCity())
                        .postalCode(zimIdResponse.getAddress().getPostcode())
                        .line1(zimIdResponse.getAddress().getStreet())
                        .line2(zimIdResponse.getAddress().getAddressaddition());
                if ("D".equals(zimIdResponse.getAddress().getCountry().toUpperCase(Locale.ROOT)))
                    address.setCountry(new AddressCountry().value("DEU"));
                Iterator<Address> iterator = user.getContactInfo().getAddress().iterator();
                while (iterator.hasNext()) {
                    Address oldAddress = iterator.next();
                    if (isSameAddress(oldAddress, address))
                        iterator.remove();
                    else
                        oldAddress.preferred(false);
                }
                user.getContactInfo().addAddressItem(address);
            }
        } else if (userNumber != 0L) {
            ReadAddressByRegistrationnumberResponse matrikelResponse = this.addressWebServiceClient.getAddressByMatrikel(userNumber);
            if (matrikelResponse != null) {
                Address address = new Address().preferred(true)
                        .addAddressTypeItem(new AddressAddressType().value("home"))
                        .city(matrikelResponse.getAddress().getCity())
                        .postalCode(matrikelResponse.getAddress().getPostcode())
                        .line1(matrikelResponse.getAddress().getStreet())
                        .line2(matrikelResponse.getAddress().getAddressaddition());
                if ("D".equals(matrikelResponse.getAddress().getCountry().toUpperCase(Locale.ROOT)))
                    address.setCountry(new AddressCountry().value("DEU"));
                Iterator<Address> iterator = user.getContactInfo().getAddress().iterator();
                while (iterator.hasNext()) {
                    Address oldAddress = iterator.next();
                    if (isSameAddress(oldAddress, address))
                        iterator.remove();
                    else
                        oldAddress.preferred(false);
                }
                user.getContactInfo().addAddressItem(address);

            }
        }  else {
            log.warn(String.format("could not get address for user %s from his system.", primaryId));
            return;
        }
        try {
            this.almaUserApiClient.updateUser(user.getPrimaryId(), user);
        } catch (
                FeignException fe) {
            log.warn("could not update user with id " + primaryId, fe);
            return;
        }
        log.info(String.format("retrieved address for user %s", primaryId));
    }

    public boolean isSameAddress(Address address, Address other) {
        try {
            if (address.getCity().equals(other.getCity()) && address.getLine1().equals(other.getLine1())) {
                if (address.getPostalCode().equals(other.getPostalCode()))
                    return true;
                else if (address.getLine3().equals(other.getPostalCode() + " " + other.getCity()))
                    return true;
                return true;
            } else
                return false;
        } catch (NullPointerException npe) {
            return false;
        }
    }
}
