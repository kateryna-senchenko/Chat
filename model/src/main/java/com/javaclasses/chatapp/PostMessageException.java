package com.javaclasses.chatapp;

/**
 * Exception that indicates failure of posting a message
 */
/*package*/public class PostMessageException extends Exception{

    public PostMessageException(String message){
        super(message);
    }
}
