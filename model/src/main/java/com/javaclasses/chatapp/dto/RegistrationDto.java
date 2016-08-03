package com.javaclasses.chatapp.dto;


/**
 * Contains data to register a new user
 */
public class RegistrationDto {

    private final String username;
    private final String password;
    private final String confirmPassword;


    public RegistrationDto(String username, String password, String confirmPassword) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationDto that = (RegistrationDto) o;

        return username.equals(that.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
