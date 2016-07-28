package com.javaclasses.chatapp;


public class TransferObject {

    private final String content;

    public TransferObject(String content) {
        this.content = content;
    }

    /* package */ String getContent() {
        return content;
    }
}
