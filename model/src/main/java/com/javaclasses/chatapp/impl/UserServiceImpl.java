package com.javaclasses.chatapp.impl;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.storage.LoggedInUserRepository;
import com.javaclasses.chatapp.storage.Repository;
import com.javaclasses.chatapp.storage.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl userService = new UserServiceImpl();
    private static Repository userRepository;
    private static Repository loggedInUserRepository;
    private AtomicLong count = new AtomicLong(0);

    private UserServiceImpl() {
        userRepository = UserRepository.getInstance();
        loggedInUserRepository = LoggedInUserRepository.getInstance();
    }

    public static UserServiceImpl getInstance() {
        return userService;
    }

    public Repository getUserRepository() {
        return userRepository;
    }

    public Repository getLoggedInUserRepository() {
        return loggedInUserRepository;
    }

    @Override
    public UserId register(String username, String password, String confirmPassword) throws RegistrationException {

        username = username.trim();

        if (username.isEmpty() || username.contains(" ")) {
            log.error("Failed to register user {}: invalid username input", username);
            throw new RegistrationException("Username should not be empty or contain white spaces");
        }

        if (password.isEmpty()) {
            log.error("Failed to register user {}: invalid password input", username);
            throw new RegistrationException("Password should not be empty");
        }


        Collection<User> allUsers = userRepository.getAll();

        for (User user : allUsers) {

            if (user.getUsername() == username) {
                log.error("Registration failed: username {} is already taken", username);
                throw new RegistrationException("Specified username is not available");
            }
        }

        if (password != confirmPassword) {
            log.error("Failed to register user {}: passwords do not match", username);
            throw new RegistrationException("Passwords do not match");
        }

        UserId newUserId = new UserId(count.getAndIncrement());

        User newUser = new User(newUserId, username, password);

        userRepository.add(newUserId, newUser);

        if(log.isInfoEnabled()){
            log.info("Registered user {}", username);
        }

        return newUserId;
    }

    @Override
    public Token login(String username, String password) throws AuthenticationException {

        Collection<User> allUsers = userRepository.getAll();

        User userToLogin = null;

        for (User user : allUsers) {

            if (user.getUsername() == username) {
                if (user.getPassword() == password) {
                    userToLogin = user;
                    break;
                }
            }
        }

        if (userToLogin == null) {
            log.error("Failed to login user {}: either username or password is incorrect", username);
            throw new AuthenticationException("Specified combination of username and password was not found");
        }

        Token newToken = new Token(System.currentTimeMillis());

        loggedInUserRepository.add(newToken, userToLogin);

         if(log.isInfoEnabled()){
            log.info("Logged in user {}", username);
        }

        return newToken;
    }
}
