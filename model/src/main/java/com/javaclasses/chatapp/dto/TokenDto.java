package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.UserId;

import java.util.UUID;

/**
 * Token DTO
 */
public class TokenDto {

    private final UUID token;
    private final UserId userId;

    public TokenDto(UUID token, UserId userId) {
        this.token = token;
        this.userId = userId;
    }

    public UUID getToken() {
        return token;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenDto token1 = (TokenDto) o;

        if (!token.equals(token1.token)) return false;
        return userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
