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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginDTO loginDTO = (LoginDTO) o;

        return username.equals(loginDTO.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
