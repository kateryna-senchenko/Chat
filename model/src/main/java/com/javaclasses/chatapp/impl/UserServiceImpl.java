package com.javaclasses.chatapp.impl;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.LoginDTO;
import com.javaclasses.chatapp.dto.RegistrationDTO;
import com.javaclasses.chatapp.dto.UserDTO;
import com.javaclasses.chatapp.Token;
import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.storage.TokenRepositoryImpl;
import com.javaclasses.chatapp.storage.Repository;
import com.javaclasses.chatapp.storage.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl userService = new UserServiceImpl();
    private static Repository<UserId, User> userRepository;
    private static Repository<UUID, Token> tokenRepository;
    private AtomicLong count = new AtomicLong(0);

    private UserServiceImpl() {
        userRepository = UserRepositoryImpl.getInstance();
        tokenRepository = TokenRepositoryImpl.getInstance();
    }

    public static UserServiceImpl getInstance() {
        return userService;
    }


    @Override
    public UserId register(RegistrationDTO registrationDTO) throws RegistrationException {

        String username = registrationDTO.getUsername().trim();

        if (username.isEmpty() || username.contains(" ")) {
            log.error("Failed to register user {}: invalid username input", username);
            throw new RegistrationException("Username should not be empty or contain white spaces");
        }

        if (registrationDTO.getPassword().isEmpty()) {
            log.error("Failed to register user {}: invalid password input", username);
            throw new RegistrationException("Password should not be empty");
        }


        Collection<User> allUsers = userRepository.getAll();

        for (User user : allUsers) {

            if (user.getUsername().equals(username)) {
                log.error("Registration failed: username {} is already taken", username);
                throw new RegistrationException("Specified username is not available");
            }
        }

        if (!(registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword()))) {
            log.error("Failed to register user {}: passwords do not match", username);
            throw new RegistrationException("Passwords do not match");
        }

        UserId newUserId = new UserId(count.getAndIncrement());

        User newUser = new User(newUserId, username, registrationDTO.getPassword());

        userRepository.add(newUserId, newUser);

        if(log.isInfoEnabled()){
            log.info("Registered user {}", username);
        }

        return newUserId;
    }

    @Override
    public Token login(LoginDTO loginDTO) throws AuthenticationException {

        Collection<User> allUsers = userRepository.getAll();

        User userToLogin = null;

        for (User user : allUsers) {

            if (user.getUsername().equals(loginDTO.getUsername())) {
                if (user.getPassword().equals(loginDTO.getPassword())) {
                    userToLogin = user;
                    break;
                }
            }
        }

        if (userToLogin == null) {
            log.error("Failed to login user {}: either username or password is incorrect", loginDTO.getUsername());
            throw new AuthenticationException("Specified combination of username and password was not found");
        }

        Token newToken = new Token(UUID.randomUUID(), userToLogin.getId());

        tokenRepository.add(newToken.getToken(), newToken);

         if(log.isInfoEnabled()){
            log.info("Logged in user {}", loginDTO.getUsername());
        }

        return newToken;
    }

    @Override
    public UserDTO findRegisteredUserById(UserId id) {

        User user = userRepository.getItem(id);
        return new UserDTO(user.getId(), user.getUsername());
    }

    @Override
    public UserDTO findLoggedInUserByToken(Token token) {

        Token userToken = tokenRepository.getItem(token.getToken());

        return findRegisteredUserById(userToken.getUserId());
    }
}
