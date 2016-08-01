package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;

import java.util.UUID;

/**
 * Access token
 */
public class Token {

    private TokenId token;
    private final UserId userId;

    public Token(UserId userId) {
        this.userId = userId;
    }

    public TokenId getToken() {
        return token;
    }

    public void setToken(TokenId token) {
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

        return token.equals(token1.token) && userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
