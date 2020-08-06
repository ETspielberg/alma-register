
package org.unidue.ub.unidue.almaregister.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.unidue.ub.alma.shared.user.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationRequest {

    private final static SimpleDateFormat pinFormat = new SimpleDateFormat("ddMM");

    public String userStatus = "";

    public String title = "";

    public String firstName = "";

    public String lastName = "";

    public String road = "";

    public String plz = "";

    public String city = "";

    public String email = "";

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date birthDate = new Date();

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

            default:{
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
            almaUser.status(new UserStatus().value("INACTIVE"))
                    .birthDate(birthDate)
                    .pinNumber(pinFormat.format(birthDate))
                    .accountType(new UserAccountType().value("INTERNAL"));
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
}
