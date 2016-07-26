package com.javaclasses.chatapp.impl;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.storage.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    //private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

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

        username = username.trim();

        if(username.isEmpty() || username.contains(" ")){
            //log.error("Registration failed: invalid username input");
            throw new RegistrationException("Username should not be empty or contain white spaces");
        }

        if(password.isEmpty()){
            //log.error("Registration failed: invalid password input");
            throw new RegistrationException("Password should not be empty");
        }


        Collection<User> allUsers = userRepository.getAll();

        for(User user: allUsers){

            if(user.getUsername() == username){
                //log.error("Registration failed: username {} is already taken", username);
                throw new RegistrationException("Specified username is not available");
            }
        }

        if(password != confirmPassword){
            //log.error("Registration failed: passwords do not match");
            throw new RegistrationException("Passwords do not match");
        }

        UserId newUserId = new UserId(count.getAndIncrement());

        User newUser = new User(newUserId, username, password);

        userRepository.add(newUserId, newUser);

       /* if(log.isInfoEnabled()){
            log.info("Registered user {}", username);
        }*/

        return newUserId;
    }

    @Override
    public Token login(String username, String password) throws AuthenticationException {
        return null;
    }
}
