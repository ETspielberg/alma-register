package org.unidue.ub.unidue.almaregister.service;

public class MissingShibbolethDataException extends RuntimeException {

    public MissingShibbolethDataException(String message) {
        super(message);
    }
}
