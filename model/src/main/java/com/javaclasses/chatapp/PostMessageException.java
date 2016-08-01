package com.javaclasses.chatapp;

/**
 * Exception that indicates failure of posting a message
 */
public class PostMessageException extends ChatAppException{

    public PostMessageException(ErrorType errorType) {
        super(errorType);
    }
}
