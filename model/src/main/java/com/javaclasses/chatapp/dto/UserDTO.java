package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.UserId;

/**
 * User DTO
 */
public class UserDTO {

    private final UserId id;

    private String username;

    public UserDTO(UserId id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return id.equals(userDTO.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
