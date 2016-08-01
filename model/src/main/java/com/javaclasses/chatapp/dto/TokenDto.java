package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * Token DTO
 */
public class TokenDto {

    private final TokenId token;
    private final UserId userId;

    public TokenDto(TokenId token, UserId userId) {
        this.token = token;
        this.userId = userId;
    }

    public TokenId getToken() {
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

        return token.equals(token1.token) && userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }
}
