package com.javaclasses.chatapp;

/**
 * Exception that indicates authentication failure
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message){
        super(message);
    }
}
