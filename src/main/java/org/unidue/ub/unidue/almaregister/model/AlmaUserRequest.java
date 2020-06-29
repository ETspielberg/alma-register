package org.unidue.ub.unidue.almaregister.model;

import org.unidue.ub.alma.shared.user.AlmaUser;

/**
 * Container object holding the Alma user to be created together with the information about the acceptance of the
 * privacy and terms
 */
public class AlmaUserRequest {

    public AlmaUser almaUser;

    public boolean isPrivacyAccepted = false;

    public boolean isTermsAccepted = false;

    public AlmaUserRequest() {
    }

    public AlmaUserRequest withAlmaUser(AlmaUser almaUser) {
        this.almaUser = almaUser;
        return this;
    }

    public boolean isPrivacyAccepted() {
        return isPrivacyAccepted;
    }

    public void setPrivacyAccepted(boolean privacyAccepted) {
        isPrivacyAccepted = privacyAccepted;
    }

    public boolean isTermsAccepted() {
        return isTermsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        isTermsAccepted = termsAccepted;
    }

    public AlmaUser getAlmaUser() {
        return almaUser;
    }

    public void setAlmaUser(AlmaUser almaUser) {
        this.almaUser = almaUser;
    }
}
