package org.unidue.ub.unidue.almaregister.model.reports;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "row")
public class Overdue {

    public final static String PATH = "/shared/Universität Duisburg-Essen 49HBZ_UDE/libintel/BenutzerNachMahnung3";


    @JacksonXmlProperty(localName = "FirstName")
    private String firstName;

    @JacksonXmlProperty(localName = "LastName")
    private String lastName;

    @JacksonXmlProperty(localName = "PrimaryIdentifier")
    private String primaryIdentifier;

    @JacksonXmlProperty(localName = "DueDate")
    private String dueDate;

    @JacksonXmlProperty(localName = "UserGroup")
    private String userGroup;

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

    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    public void setPrimaryIdentifier(String primaryIdentifier) {
        this.primaryIdentifier = primaryIdentifier;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }
}