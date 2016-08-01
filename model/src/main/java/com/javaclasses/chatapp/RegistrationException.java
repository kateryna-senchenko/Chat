package com.javaclasses.chatapp;

/**
 * Exception that indicates registration failure
 */
public class RegistrationException extends ChatAppException {

    public RegistrationException(ErrorType errorType) {
        super(errorType);
    }
}
