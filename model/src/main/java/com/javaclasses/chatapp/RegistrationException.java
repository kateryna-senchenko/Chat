package com.javaclasses.chatapp;

/**
 * Exception that indicates registration failure
 */
public class RegistrationException extends Exception {

    private enum ErrorTypes{

        DUPLICATE_USERNAME,
        USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES,
        PASSWORD_IS_EMPTY,
        PASSWORDS_DO_NOT_MATCH
    }

    public RegistrationException(String message){
        super(message);
    }
}
