package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * User DTO
 */
public class UserDto {

    private final UserId id;
    private final String username;

    public UserDto(UserId id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDto userDto = (UserDto) o;

        return id.equals(userDto.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
