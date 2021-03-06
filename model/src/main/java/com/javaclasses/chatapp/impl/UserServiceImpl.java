package com.javaclasses.chatapp.impl;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.*;
import com.javaclasses.chatapp.entities.Token;
import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.storage.TokenRepositoryImpl;
import com.javaclasses.chatapp.storage.Repository;
import com.javaclasses.chatapp.storage.UserRepositoryImpl;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;


import static com.javaclasses.chatapp.ErrorType.*;

/**
 * Implementation of the UserService Interface
 */
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private static UserServiceImpl userService = new UserServiceImpl();

    private static Repository<UserId, User> userRepository;
    private static Repository<TokenId, Token> tokenRepository;

    private static ChatService chatService;

    private UserServiceImpl() {
        userRepository = UserRepositoryImpl.getInstance();
        tokenRepository = TokenRepositoryImpl.getInstance();
        chatService = ChatServiceImpl.getInstance();
    }

    public static UserService getInstance() {
        return userService;
    }


    @Override
    public UserId register(RegistrationDto registrationDto) throws RegistrationException {

        String username = registrationDto.getUsername().trim();

        if (username.isEmpty() || username.contains(" ")) {
            log.error("Failed to register user {}: invalid username input", username);
            throw new RegistrationException(USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES);
        }

        if (registrationDto.getPassword().isEmpty()) {
            log.error("Failed to register user {}: invalid password input", username);
            throw new RegistrationException(PASSWORD_IS_EMPTY);
        }


        Collection<User> allUsers = userRepository.getAll();

        for (User user : allUsers) {

            if (user.getUsername().equals(username)) {
                log.error("Registration failed: username {} is already taken", username);
                throw new RegistrationException(DUPLICATE_USERNAME);
            }
        }

        if (!(registrationDto.getPassword().equals(registrationDto.getConfirmPassword()))) {
            log.error("Failed to register user {}: passwords do not match", username);
            throw new RegistrationException(PASSWORDS_DO_NOT_MATCH);
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
            throw new AuthenticationException(AUTHENTICATION_FAILED);
        }

        Token newToken = new Token(userToLogin.getId());

        TokenId token = tokenRepository.add(newToken);
        newToken.setTokenId(token);

        if (log.isInfoEnabled()) {
            log.info("Logged in user {}", loginDto.getUsername());
        }

        return new TokenDto(newToken.getTokenId(), newToken.getUserId());
    }

    @Override
    public UserDto findRegisteredUserById(UserId id) {

        User user = userRepository.getItem(id);

        return new UserDto(user.getId(), user.getUsername());
    }

    @Override
    public UserDto findLoggedInUserByToken(TokenDto token) {

        Token userToken = tokenRepository.getItem(token.getTokenId());

        return findRegisteredUserById(userToken.getUserId());
    }

    @Override
    public void deleteUser(UserId id) throws UserRemovalException{

        Collection<ChatDto> chatDtos = chatService.findAllChats();

        for(ChatDto chat: chatDtos){
            List<UserId> members = chat.getMembers();
            if(members.contains(id)){
                throw new UserRemovalException(USER_IS_A_MEMBER_OF_CHAT);
            }
        }

        userRepository.remove(id);

        if (log.isInfoEnabled()) {
            log.info("Removed user {}", id.getId());
        }
    }

    @Override
    public void logout(TokenDto token) {

        tokenRepository.remove(token.getTokenId());

        if (log.isInfoEnabled()) {
            log.info("Logged out user {}", token.getUserId().getId());
        }

    }
}
