package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * User DTO
 */
public class UserEntityDto {

    private final UserId id;
    private final String username;

    public UserEntityDto(UserId id, String username) {
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

        UserEntityDto userEntityDto = (UserEntityDto) o;

        return id.equals(userEntityDto.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
