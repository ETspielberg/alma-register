
package org.unidue.ub.unidue.almaregister.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.unidue.ub.alma.shared.user.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * A POJO holding all the necessary information to create an AlmaUser request.
 */
public class RegistrationRequest {

    private final static SimpleDateFormat pinFormat = new SimpleDateFormat("ddMMyyyy");

    public String userStatus = "";

    public String gender = "";

    public String title = "";

    public String firstName = "";

    public String lastName = "";

    public String road = "";

    public String plz = "";

    public String city = "";

    public String email = "";

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthDate = LocalDate.now();

    public String password = "";

    public String passwordRepeat = "";

    public boolean privacyAccepted = false;

    public boolean termsAccepted = false;

    public String primaryId = "";

    public String externalId = "";

    public RegistrationRequest() {
    }

    public static SimpleDateFormat getPinFormat() {
        return pinFormat;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public boolean getPrivacyAccepted() {
        return privacyAccepted;
    }

    public void setPrivacyAccepted(boolean privacyAccepted) {
        this.privacyAccepted = privacyAccepted;
    }

    public boolean getTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * creates an AlmaUser object to be submitted to Alma Users API from the supplied personal data.
     *
     * @return an AlmaUser object
     */
    public AlmaUser getAlmaUser() {
        AlmaUser almaUser = new AlmaUser()
                .lastName(lastName)
                .firstName(firstName);
        switch (userStatus) {
            case "staff": {
                almaUser.setUserGroup(new UserUserGroup().value("06"));
                break;
            }
            case "student": {
                almaUser.setUserGroup(new UserUserGroup().value("01"));
                break;
            }
            case "extern": {
                almaUser.setUserGroup(new UserUserGroup().value("22"));
                break;
            }

            case "UNDRGRD": {
                almaUser.setUserGroup(new UserUserGroup().value("20"));
                break;
            }
            case "other": {
                almaUser.setUserGroup(null);
                break;
            }
            default: {
                almaUser.setUserGroup(new UserUserGroup().value("22"));
            }

        }
        Email emailAddress = new Email();
        ContactInfo contactInfo = new ContactInfo();
        if (primaryId.isEmpty()) {
            Address postalAddress = new Address()
                    .city(city)
                    .postalCode(plz)
                    .preferred(true)
                    .line1(road)
                    .addAddressTypeItem(new AddressAddressType().value("home"));
            emailAddress
                    .emailAddress(email)
                    .addEmailTypeItem(new EmailEmailType().value("personal"))
                    .preferred(true);
            contactInfo.addEmailItem(emailAddress)
                    .addAddressItem(postalAddress);

            Date birthday = dateFromLocalDate(birthDate);
            Date expiryDate;
            if ("other".equals(userStatus)) {
                expiryDate = dateFromLocalDate(LocalDate.now());
            } else {
                expiryDate = dateFromLocalDate(LocalDate.now().plusYears(1).plusWeeks(1));
            }
            UserIdentifierIdType userIdentifierIdType = new UserIdentifierIdType().value("01");
            int random = (int) (Math.random() * 1000000);
            UserIdentifier userIdentifier = new UserIdentifier().idType(userIdentifierIdType).status("INACTIVE").value("NEU-" + random).segmentType("internal");
            almaUser.status(new UserStatus().value("INACTIVE"))
                    .gender(new UserGender().value(gender))
                    .addUserIdentifierItem(userIdentifier)
                    .birthDate(birthday)
                    .password(password)
                    .pinNumber(pinFormat.format(birthday))
                    .accountType(new UserAccountType().value("INTERNAL"))
                    .setExpiryDate(expiryDate);
        } else {
            emailAddress
                    .emailAddress(email)
                    .addEmailTypeItem(new EmailEmailType().value("personal"))
                    .preferred(true);
            contactInfo.addEmailItem(emailAddress)
                    .setAddress(null);
            almaUser.status(new UserStatus().value("ACTIVE"))
                    .accountType(new UserAccountType().value("EXTERNAL"))
                    .externalId(externalId);
            UserIdentifierIdType userIdentifierIdType = new UserIdentifierIdType().value("03");
            UserIdentifier userIdentifier = new UserIdentifier().idType(userIdentifierIdType).status("ACTIVE").value(primaryId).segmentType("internal");
            almaUser.addUserIdentifierItem(userIdentifier);
        }
        return almaUser.contactInfo(contactInfo);
    }

    private Date dateFromLocalDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.of("GMT");
        return Date.from(localDate.atStartOfDay().atZone(defaultZoneId).toInstant());
    }
}
