package com.javaclasses.chatapp.impl;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.dto.UserDto;
import com.javaclasses.chatapp.entities.Token;
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


    private UserServiceImpl() {
        userRepository = UserRepositoryImpl.getInstance();
        tokenRepository = TokenRepositoryImpl.getInstance();
    }

    public static UserService getInstance() {
        return userService;
    }


    @Override
    public UserId register(RegistrationDto registrationDto) throws RegistrationException {

        String username = registrationDto.getUsername().trim();

        if (username.isEmpty() || username.contains(" ")) {
            log.error("Failed to register user {}: invalid username input", username);
            throw new RegistrationException("Username should not be empty or contain white spaces");
        }

        if (registrationDto.getPassword().isEmpty()) {
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

        if (!(registrationDto.getPassword().equals(registrationDto.getConfirmPassword()))) {
            log.error("Failed to register user {}: passwords do not match", username);
            throw new RegistrationException("Passwords do not match");
        }


        User newUser = new User(username, registrationDto.getPassword());
        UserId newUserId = userRepository.add(newUser);
        newUser.setId(newUserId);

        if (log.isInfoEnabled()) {
            log.info("Registered user {}", username);
        }

        return newUserId;
    }

    @Override
    public TokenDto login(LoginDto loginDto) throws AuthenticationException {

        Collection<User> allUsers = userRepository.getAll();

        User userToLogin = null;


        for (User user : allUsers) {

            if (user.getUsername().equals(loginDto.getUsername())) {
                if (user.getPassword().equals(loginDto.getPassword())) {
                    userToLogin = user;
                    break;
                }
            }
        }

        if (userToLogin == null) {
            log.error("Failed to login user {}: either username or password is incorrect", loginDto.getUsername());
            throw new AuthenticationException("Specified combination of username and password was not found");
        }

        Token newToken = new Token(userToLogin.getId());

        UUID token = tokenRepository.add(newToken);
        newToken.setToken(token);

        if (log.isInfoEnabled()) {
            log.info("Logged in user {}", loginDto.getUsername());
        }

        return new TokenDto(newToken.getToken(), newToken.getUserId());
    }

    @Override
    public UserDto findRegisteredUserById(UserId id) {

        User user = userRepository.getItem(id);
        return new UserDto(user.getId(), user.getUsername());
    }

    @Override
    public UserDto findLoggedInUserByToken(TokenDto token) {

        Token userToken = tokenRepository.getItem(token.getToken());

        return findRegisteredUserById(userToken.getUserId());
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();

        if (log.isInfoEnabled()) {
            log.info("User Repository and Token Repository have been cleared");
        }
    }
}
