package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.entities.Token;
import com.javaclasses.chatapp.tinytypes.TokenId;

import java.util.UUID;

/**
 * Implementation of the Repository interface for logged in user repository
 */
public class TokenRepositoryImpl extends InMemoryRepository<TokenId, Token> {

    private static Repository<TokenId, Token> tokenRepository = new TokenRepositoryImpl();

    private TokenRepositoryImpl() {}

    public static Repository<TokenId, Token> getInstance() {
        return tokenRepository;
    }

    @Override
    public TokenId generateId() {
        return new TokenId(UUID.randomUUID());
    }
}

