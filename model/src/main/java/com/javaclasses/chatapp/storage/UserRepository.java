package com.javaclasses.chatapp.storage;

/**
 * Implementation of the Repository interface for User repository
 */
public class UserRepository<UserId, User> extends InMemoryRepository<UserId, User> {

    private static Repository userRepository = new UserRepository();

    private UserRepository() {}

    public static Repository getInstance() {
        return userRepository;
    }
}
