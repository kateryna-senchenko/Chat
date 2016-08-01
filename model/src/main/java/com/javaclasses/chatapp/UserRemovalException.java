package com.javaclasses.chatapp;

/**
 * Exception that indicates user removal failure
 */
public class UserRemovalException extends ChatAppException {

    public UserRemovalException(ErrorType errorType) {
        super(errorType);
    }
}
