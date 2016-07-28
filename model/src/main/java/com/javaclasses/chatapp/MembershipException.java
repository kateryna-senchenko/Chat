package com.javaclasses.chatapp;

/**
 * Exception to indicate the failure of joining/leaving chat
 */
public class MembershipException extends Exception {

    public MembershipException(String message) {
        super(message);
    }
}
