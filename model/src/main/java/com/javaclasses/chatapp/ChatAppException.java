package com.javaclasses.chatapp;

/**
 * Abstract class for chat app exceptions
 */
/*package*/ abstract class ChatAppException extends Exception{

    private final ErrorType errorType;

    /*package*/ ChatAppException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    /*package*/ ErrorType getErrorType() {
        return errorType;
    }
}
