package com.javaclasses.chatapp;

/**
 * Exception that indicates registration failure
 */
public class RegistrationException extends Exception {


    public RegistrationException(String message){
        super(message);
    }
}
