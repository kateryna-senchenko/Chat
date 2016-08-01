package com.javaclasses.chatapp;

/**
 * Exception that indicates authentication failure
 */
public class AuthenticationException extends ChatAppException {

    public AuthenticationException(ErrorType errorType){
        super(errorType);
    }
}
