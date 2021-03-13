package org.unidue.ub.unidue.almaregister.service.exceptions;

/**
 * Exception thrown if the necessary data could not be obtained from the shibboleth response
 */
public class MissingShibbolethDataException extends RuntimeException {

    public MissingShibbolethDataException(String message) {
        super(message);
    }
}
