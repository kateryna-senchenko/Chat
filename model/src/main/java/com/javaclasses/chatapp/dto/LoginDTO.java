package com.javaclasses.chatapp.dto;

/**
 * Login DTO
 */
public class LoginDTO {

    private final String username;
    private final String password;


    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
