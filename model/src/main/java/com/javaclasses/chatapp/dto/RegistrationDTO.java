package com.javaclasses.chatapp.dto;

/**
 * Registration DTO
 */
public class RegistrationDTO {

    private final String username;
    private final String password;
    private final String confirmPassword;


    public RegistrationDTO(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
