package com.javaclasses.chatapp.tinytypes;

import java.util.UUID;

/**
 * Custom data type for token id
 */
public class TokenId {

    private final UUID id;

    public TokenId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenId tokenId = (TokenId) o;

        return id.equals(tokenId.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
