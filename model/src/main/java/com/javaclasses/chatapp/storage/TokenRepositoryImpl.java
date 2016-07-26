package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.Token;
import com.javaclasses.chatapp.User;

/**
 * Implementation of the Repository interface for logged in user repository
 */
public class TokenRepositoryImpl extends InMemoryRepository<Token, User> {

    private static Repository<Token, User> loggedInUserRepository = new TokenRepositoryImpl();

    private TokenRepositoryImpl() {}

    public static Repository<Token, User> getInstance() {
        return loggedInUserRepository;
    }
}

