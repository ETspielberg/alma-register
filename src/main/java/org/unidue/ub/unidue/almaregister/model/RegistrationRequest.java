
package org.unidue.ub.unidue.almaregister.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.unidue.ub.alma.shared.user.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrationRequest {

    private final static SimpleDateFormat pinFormat = new SimpleDateFormat("ddMM");

    public String userStatus = "";

    public String campus = "";

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

    public RegistrationRequest() {}

    public AlmaUser getAlmaUser() {
        Address postalAddress = new Address()
                .city(city)
                .postalCode(plz)
                .preferred(true)
                .line1(road)
                .addAddressTypeItem(new AddressAddressType().value("home"));
        Email emailAddress = new Email()
                .emailAddress(email)
                .addEmailTypeItem(new EmailEmailType().value("personal"))
                .preferred(true);
        ContactInfo contactInfo = new ContactInfo()
                .addEmailItem(emailAddress)
                .addAddressItem(postalAddress);
        AlmaUser almaUser = new AlmaUser()
                .lastName(lastName)
                .firstName(firstName)
                .userGroup(new UserUserGroup().value(userStatus))
                //.userTitle(new UserUserTitle().value(title))
                //.password(password)
                .birthDate(birthDate)
                .pinNumber(pinFormat.format(birthDate))
                //.campusCode(new UserCampusCode().value(campus))
                .contactInfo(contactInfo)
                .status(new UserStatus().value("INACTIVE"))
                .accountType(new UserAccountType().value("INTERNAL"));
        return almaUser;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
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
}
