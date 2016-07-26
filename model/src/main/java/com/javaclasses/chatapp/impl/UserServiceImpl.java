package com.javaclasses.chatapp.impl;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.storage.UserRepository;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    private static UserServiceImpl userService = new UserServiceImpl();
    private static UserRepository userRepository;
    private AtomicLong count = new AtomicLong(0);

    private UserServiceImpl(){
        userRepository = UserRepository.getInstance();
    }

    public static UserServiceImpl getInstance( ) {
        return userService;
    }

    public UserRepository getRepository(){
        return userRepository;
    }

    @Override
    public UserId register(String username, String password, String confirmPassword) throws RegistrationException {

        username.trim();

        if(username.isEmpty() || username.contains(" ")){
            throw new RegistrationException("Username should not be empty or contain white spaces");
        }

        if(password.isEmpty()){
            throw new RegistrationException("Password should not be empty");
        }


        Collection<User> allUsers = userRepository.getAll();

        for(User user: allUsers){

            if(user.getUsername() == username){
                throw new RegistrationException("Specified username is not available");
            }
        }

        if(password != confirmPassword){
            throw new RegistrationException("Passwords do not match");
        }

        UserId newUserId = new UserId(count.getAndIncrement());

        User newUser = new User(newUserId, username, password);

        userRepository.add(newUserId, newUser);

        return newUserId;
    }

    @Override
    public Token login(String username, String password) throws AuthenticationException {
        return null;
    }
}
