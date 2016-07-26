package com.javaclasses.chatapp.impl;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.storage.Repository;
import com.javaclasses.chatapp.storage.UserRepository;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    private static UserServiceImpl userService = new UserServiceImpl();
    private static Repository userRepository;

    private UserServiceImpl(){
        userRepository = UserRepository.getInstance();
    }

    public static UserServiceImpl getInstance( ) {
        return userService;
    }

    public Repository getRepository(){
        return userRepository;
    }

    @Override
    public UserId register(String username, String password, String confirmPassword) throws RegistrationException {
        return null;
    }

    @Override
    public Token login(String username, String password) throws AuthenticationException {
        return null;
    }
}
