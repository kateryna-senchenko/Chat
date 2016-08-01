package com.javaclasses.chatapp;

/**
 * Enum of error types and corresponding messages
 */
public enum ErrorType {

    DUPLICATE_USERNAME("Specified username is not available"),
    USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES("Username should not be empty or contain white spaces"),
    PASSWORD_IS_EMPTY("Password should not be empty"),
    PASSWORDS_DO_NOT_MATCH("Passwords do not match"),

    AUTHENTICATION_FAILED("Specified combination of username and password was not found"),

    CHATNAME_IS_EMPTY("Chat name should not be empty"),
    DUPLICATE_CHATNAME("Specified name is not available"),
    ALREADY_A_MEMBER("User is already a member"),
    IS_NOT_A_MEMBER("User is not a member"),

    USER_IS_A_MEMBER_OF_CHAT("User ia a member of chat");




    private final String message;

    ErrorType(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }
}
