package com.javaclasses.chatapp;

/**
 * Abstract class for chat app exceptions
 */
public abstract class ChatAppException extends Exception{

    private final ErrorType errorType;

    public ChatAppException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
