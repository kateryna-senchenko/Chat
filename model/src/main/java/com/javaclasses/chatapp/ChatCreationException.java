package com.javaclasses.chatapp;

/**
 * Exception that indicates failure of a chat creation
 */
public class ChatCreationException extends Exception {

    public ChatCreationException(String message) {
        super(message);
    }
}
