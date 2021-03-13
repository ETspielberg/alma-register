package org.unidue.ub.unidue.almaregister.service.exceptions;

/**
 * Exception thrown if errors occur upon trying to connect to Alma
 */
public class AlmaConnectionException extends RuntimeException {

    public AlmaConnectionException(String message) {super(message);}
}
