package com.javaclasses.chatapp;

/**
 * Exception that indicates failure of posting a message
 */
/*package*/class PostMessageException extends Exception{

    public PostMessageException(String message){
        super(message);
    }
}
