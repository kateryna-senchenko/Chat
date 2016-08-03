package com.javaclasses.chatapp.dto;


/**
 * Contains data to login user
 */
public class LoginDto {

    private final String username;
    private final String password;


    public LoginDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginDto loginDto = (LoginDto) o;

        return username.equals(loginDto.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
