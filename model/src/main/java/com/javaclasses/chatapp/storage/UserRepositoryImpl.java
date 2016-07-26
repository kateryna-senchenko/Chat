package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.UserId;

/**
 * Implementation of the Repository interface for User repository
 */
public class UserRepositoryImpl extends InMemoryRepository<UserId, User> {

    private static Repository<UserId, User> userRepository = new UserRepositoryImpl();

    private UserRepositoryImpl() {}

    public static Repository<UserId, User> getInstance() {
        return userRepository;
    }
}
