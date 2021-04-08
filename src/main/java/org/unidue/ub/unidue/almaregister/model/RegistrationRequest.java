
package org.unidue.ub.unidue.almaregister.model;

import org.springframework.format.annotation.DateTimeFormat;
import org.unidue.ub.alma.shared.user.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public LocalDate birthDate;

    public String password = "";

    public String campus = "";

    public String passwordRepeat = "";

    public boolean privacyAccepted = false;

    public boolean termsAccepted = false;

    public String primaryId = "";

    public String externalId = "";

    public String matrikelNumber = "";

    public int cardCurrens = 0;

    public String cardNumber = "";

    public String roomNumber = "";

    public List<String> additionalEmailAdresses = new ArrayList<>();

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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getMatrikelNumber() {
        return matrikelNumber;
    }

    public void setMatrikelNumber(String matrikelNumber) {
        this.matrikelNumber = matrikelNumber;
    }

    public int getCardCurrens() {
        return cardCurrens;
    }

    public void setCardCurrens(int cardCurrens) {
        this.cardCurrens = cardCurrens;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    /**
     * creates an AlmaUser object to be submitted to Alma Users API from the supplied personal data.
     *
     * @return an AlmaUser object
     */
    public AlmaUser getAlmaUser(String language) {
        AlmaUser almaUser = new AlmaUser()
                .lastName(lastName)
                .firstName(firstName)
                .preferredLanguage(new UserPreferredLanguage().value(language));
        if ("other".equals(userStatus))
            almaUser.setUserGroup(null);
        else
            almaUser.setUserGroup(new UserUserGroup().value(userStatus));
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
                    .userTitle(new UserUserTitle().value(title))
                    .gender(new UserGender().value(gender))
                    .addUserIdentifierItem(userIdentifier)
                    .birthDate(birthday)
                    .password(password)
                    .pinNumber(pinFormat.format(birthday))
                    .accountType(new UserAccountType().value("INTERNAL"))
                    .setExpiryDate(expiryDate);
        } else {

            // set birthday amd pin-number
            if (birthDate != null) {
                Date birthday = dateFromLocalDate(birthDate);
                almaUser.pinNumber(pinFormat.format(birthday))
                        .birthDate(birthday);
            }
            //set address data for coworkers
            if ("06".equals(userStatus)) {
                Address postalAddress = new Address()
                        .city(city)
                        .line1(roomNumber)
                        .line2(campus)
                        .preferred(true)
                        .addAddressTypeItem(new AddressAddressType().value("work"));
                contactInfo.addAddressItem(postalAddress);
            }
            emailAddress
                    .emailAddress(email)
                    .addEmailTypeItem(new EmailEmailType().value("personal"))
                    .preferred(true);
            contactInfo.addEmailItem(emailAddress);
            for (String additionalEmailAddress : this.additionalEmailAdresses) {
                Email addEmail = new Email().emailAddress(additionalEmailAddress)
                        .addEmailTypeItem(new EmailEmailType().value("personal"))
                        .preferred(false);
                contactInfo.addEmailItem(addEmail);
            }

            almaUser.status(new UserStatus().value("ACTIVE"))
                    .userTitle(new UserUserTitle().value(title))
                    .accountType(new UserAccountType().value("EXTERNAL"))
                    .externalId(externalId);

            if (!cardNumber.isEmpty()) {
                UserIdentifierIdType userIdentifierIdTypeCard = new UserIdentifierIdType().value("01");
                UserIdentifier cardIdentifier = new UserIdentifier().idType(userIdentifierIdTypeCard).status("ACTIVE").value(cardNumber).segmentType("external");
                almaUser.addUserIdentifierItem(cardIdentifier);
            }
            UserIdentifierIdType userIdentifierIdType = new UserIdentifierIdType().value("03");
            UserIdentifier userIdentifier = new UserIdentifier().idType(userIdentifierIdType).status("ACTIVE").value(primaryId).segmentType("external");
            almaUser.addUserIdentifierItem(userIdentifier);
        }        return almaUser.contactInfo(contactInfo);
    }

    private Date dateFromLocalDate(LocalDate localDate) {
        ZoneId defaultZoneId = ZoneId.of("GMT");
        return Date.from(localDate.atStartOfDay().atZone(defaultZoneId).toInstant());
    }
}
