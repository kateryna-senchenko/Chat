package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * Access Token entity
 */
public class Token {

    private TokenId tokenId;
    private final UserId userId;

    public Token(UserId userId) {
        this.userId = userId;
    }

    public TokenId getTokenId() {
        return tokenId;
    }

    public void setTokenId(TokenId tokenId) {
        this.tokenId = tokenId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token token1 = (Token) o;

        return tokenId.equals(token1.tokenId) && userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
