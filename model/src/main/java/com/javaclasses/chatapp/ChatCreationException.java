package com.javaclasses.chatapp;

/**
 * Exception that indicates failure of a chat creation
 */
public class ChatCreationException extends ChatAppException {

    public ChatCreationException(ErrorType errorType) {
        super(errorType);
    }
}
