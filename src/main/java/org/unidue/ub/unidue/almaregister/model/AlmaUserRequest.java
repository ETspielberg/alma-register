package org.unidue.ub.unidue.almaregister.model;

public class AlmaUserRequest {

    private String email = "";

    private String preferredEmail = "";

    private String internalId = "";

    private String affiliation = "";

    private String firstName = "";

    private String surname = "";

    public AlmaUserRequest() {
    }

    public AlmaUserRequest withEmail(String email) {
        this.email = email;
        return this;
    }

    public AlmaUserRequest withInternalId(String internalId) {
        this.internalId = internalId;
        return this;
    }

    public AlmaUserRequest withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AlmaUserRequest withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public AlmaUserRequest withAffiliation(String affiliation) {
        this.affiliation = affiliation;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPreferredEmail() {
        return preferredEmail;
    }

    public void setPreferredEmail(String preferredEmail) {
        this.preferredEmail = preferredEmail;
    }
}
