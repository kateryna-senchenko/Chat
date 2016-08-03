package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * Token DTO
 */
public class TokenDto {

    private final TokenId tokenId;
    private final UserId userId;

    public TokenDto(TokenId tokenId, UserId userId) {
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public TokenId getTokenId() {
        return tokenId;
    }

    public UserId getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenDto token1 = (TokenDto) o;

        return tokenId.equals(token1.tokenId) && userId.equals(token1.userId);

    }

    @Override
    public int hashCode() {
        return tokenId.hashCode();
    }
}
