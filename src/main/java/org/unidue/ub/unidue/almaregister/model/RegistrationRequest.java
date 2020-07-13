
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

    public String primaryId = "";

    public String externalId = "";

    public RegistrationRequest() {
    }

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
                .contactInfo(contactInfo);
        if (primaryId.isEmpty()) {
            almaUser.status(new UserStatus().value("INACTIVE"))
                    .accountType(new UserAccountType().value("INTERNAL"));
        } else {
            almaUser.status(new UserStatus().value("ACTIVE"))
                    .accountType(new UserAccountType().value("EXTERNAL"))
                    .primaryId(primaryId)
                    .externalId(externalId);
        }
        return almaUser;
    }
}
