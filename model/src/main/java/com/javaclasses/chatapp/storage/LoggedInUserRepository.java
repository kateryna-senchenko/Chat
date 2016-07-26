package com.javaclasses.chatapp.storage;

/**
 * Implementation of the Repository interface for logged in user repository
 */
public class LoggedInUserRepository<Token, User> extends InMemoryRepository {

    private static Repository loggedInUserRepository = new LoggedInUserRepository();

    private LoggedInUserRepository() {}

    public static Repository getInstance() {
        return loggedInUserRepository;
    }
}

