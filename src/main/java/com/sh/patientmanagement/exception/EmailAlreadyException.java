package com.sh.patientmanagement.exception;

public class EmailAlreadyException extends RuntimeException {
    public EmailAlreadyException(String message) {
        super(message);
    }
}
