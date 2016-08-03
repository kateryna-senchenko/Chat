package com.javaclasses.chatapp.controllers;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

import static com.javaclasses.chatapp.Parameters.*;
import static com.javaclasses.chatapp.Parameters.ERROR_MESSAGE;
import static com.javaclasses.chatapp.Parameters.USERNAME;

public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private static UserController userController = new UserController();

    private final UserService userService = UserServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController() {
        registerUser();
        loginUser();
        deleteAccount();
        logoutUser();
    }

    public static UserController getInstance() {
        return userController;
    }

    private void registerUser() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/registration", "POST"), request -> {

            final String username = request.getParameter(USERNAME);
            final String password = request.getParameter(PASSWORD);
            final String confirmPassword = request.getParameter(CONFIRM_PASSWORD);

            final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

            HandlerProcessingResult handlerProcessingResult;
            try {
                UserId userId = userService.register(registrationDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(USERNAME, userService.findRegisteredUserById(userId).getUsername());
                handlerProcessingResult.setData(MESSAGE, "User " + username + " has been successfully registered");

                if(log.isInfoEnabled()){
                    log.info("Registered user {}", username);
                }
            } catch (RegistrationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if(log.isInfoEnabled()){
                    log.info("Failed to register user {}", username);
                }
            }

            return handlerProcessingResult;
        });
    }

    private void loginUser() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/login", "POST"), request -> {

            final String username = request.getParameter(USERNAME);
            final String password = request.getParameter(PASSWORD);

            final LoginDto loginDto = new LoginDto(username, password);

            HandlerProcessingResult handlerProcessingResult;

            try {
                TokenDto token = userService.login(loginDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(TOKEN_ID, String.valueOf(token.getToken().getId()));
                handlerProcessingResult.setData(USER_ID, String.valueOf(token.getUserId().getId()));
                handlerProcessingResult.setData(USERNAME, userService.findLoggedInUserByToken(token).getUsername());

                if(log.isInfoEnabled()){
                    log.info("Logged in user {}", username);
                }

            } catch (AuthenticationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if(log.isInfoEnabled()){
                    log.info("Failed to login user {}", username);
                }
            }

            return handlerProcessingResult;
        });
    }

    private void deleteAccount() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/delete-account", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final String userId = request.getParameter(USER_ID);

            final UserId id = new UserId(Long.valueOf(userId));
            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), id);
            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if(log.isInfoEnabled()){
                    log.info("Forbidden account deletion operation");
                }
            }

            try {
                userService.deleteUser(id);
                userService.logout(tokenDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(USER_ID, String.valueOf(id.getId()));

                if(log.isInfoEnabled()){
                    log.info("Deleted user {} account", userId);
                }
            } catch (UserRemovalException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if(log.isInfoEnabled()){
                    log.info("Failed to delete user {} account", userId);
                }
            }

            return handlerProcessingResult;
        });
    }

    private void logoutUser() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/logout", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final String userId = request.getParameter(USER_ID);

            final UserId id = new UserId(Long.valueOf(userId));
            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), id);
            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if(log.isInfoEnabled()){
                    log.info("Forbidden logout operation");
                }
            }

            userService.logout(tokenDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(USER_ID, String.valueOf(id.getId()));

            if(log.isInfoEnabled()){
                log.info("Logged out user {}", userId);
            }

            return handlerProcessingResult;
        });
    }
}
