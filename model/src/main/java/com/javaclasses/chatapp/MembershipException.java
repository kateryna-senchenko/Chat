package com.javaclasses.chatapp;

/**
 * Exception to indicate the failure of joining/leaving chat
 */
public class MembershipException extends ChatAppException {

    public MembershipException(ErrorType errorType) {
        super(errorType);
    }
}
