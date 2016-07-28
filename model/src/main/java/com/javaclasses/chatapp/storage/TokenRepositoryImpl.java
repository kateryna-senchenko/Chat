package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.entities.Token;

import java.util.UUID;

/**
 * Implementation of the Repository interface for logged in user repository
 */
public class TokenRepositoryImpl extends InMemoryRepository<UUID, Token> {

    private static Repository<UUID, Token> tokenRepository = new TokenRepositoryImpl();

    private TokenRepositoryImpl() {}

    public static Repository<UUID, Token> getInstance() {
        return tokenRepository;
    }

    @Override
    public UUID generateId() {
        return UUID.randomUUID();
    }
}

