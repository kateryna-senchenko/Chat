package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.UserId;

import java.util.UUID;

/**
 * Access token
 */
public class Token {

    private UUID token;
    private final UserId userId;

    public Token(UserId userId) {
        this.userId = userId;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        if (!token.equals(token1.token)) return false;
        return userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
