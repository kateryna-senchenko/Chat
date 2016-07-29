package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.tinytypes.UserId;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the Repository interface for User repository
 */
public class UserRepositoryImpl extends InMemoryRepository<UserId, User> {

    private AtomicLong count = new AtomicLong(0);

    private static Repository<UserId, User> userRepository = new UserRepositoryImpl();

    private UserRepositoryImpl() {}

    public static Repository<UserId, User> getInstance() {
        return userRepository;
    }

    @Override
    public UserId generateId() {
        return new UserId(count.getAndIncrement());
    }
}
