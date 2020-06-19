
package org.unidue.ub.unidue.almaregister.model;

import org.unidue.ub.alma.shared.user.*;

import java.util.Date;

public class RegistrationRequest {

    public String userStatus = "";

    public String campus = "";

    public String title = "";

    public String firstName = "";

    public String lastName = "";

    public String road = "";

    public String plz = "";

    public String city = "";

    public String email = "";

    public Date birthDate;

    public String password = "";

    public String passwordRepeat = "";

    public boolean isPrivacyAccepted = false;

    public boolean isTermsAccepted = false;

    public RegistrationRequest() {}

    public AlmaUser getAlmaUser() {
        Address address = new Address().city(city).postalCode(plz).preferred(true).line1(road);
        ContactInfo contactInfo = new ContactInfo()
                .addEmailItem(new Email().emailAddress(email)).addAddressItem(address);
        AlmaUser almaUser = new AlmaUser()
                .lastName(lastName)
                .firstName(firstName)
                .userGroup(new UserUserGroup().value(userStatus))
                .userTitle(new UserUserTitle().value(title))
                .password(password)
                .birthDate(birthDate)
                .campusCode(new UserCampusCode().value(campus))
                .contactInfo(contactInfo);
        return almaUser;
    }

}
