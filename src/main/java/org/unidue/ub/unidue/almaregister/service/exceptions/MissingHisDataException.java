package org.unidue.ub.unidue.almaregister.service.exceptions;

/**
 * Exception thrown if no data could be obtained from the student system
 */
public class MissingHisDataException extends RuntimeException {

    public MissingHisDataException(String message) {
        super(message);
    }
}
