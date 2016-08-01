package com.javaclasses.chatapp;

/**
 * Enum of request/response parameters
 */
public enum Parameters {

    USERNAME("username"),
    PASSWORD("password"),
    CONFIRM_PASSWORD("confirmPassword"),

    USER_ID("userId"),
    ERROR_MESSAGE("errorMessage"),
    TOKEN_ID("tokenId"),

    CHAT_NAME("chatName"),
    CHAT_ID("chatId"),

    CHAT_MESSAGE("chatMessage");



    private final String parameterName;

    Parameters(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getName() {
        return parameterName;
    }
}
