package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletResponse;

import static com.javaclasses.chatapp.Parameters.*;
import static com.javaclasses.chatapp.Parameters.ERROR_MESSAGE;
import static com.javaclasses.chatapp.Parameters.USERNAME;

public class UserController {

    private static UserController userController = new UserController();

    private final UserService userService = UserServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private UserController(){
        registerUser();
        loginUser();
    }

    public static UserController getInstance(){
        return userController;
    }

    private void registerUser(){
        handlerRegistry.registerHandler(new CompoundKey("/registration", "POST"), request -> {
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
            } catch (RegistrationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }

            return handlerProcessingResult;
        });
    }

    private void loginUser(){
        handlerRegistry.registerHandler(new CompoundKey("/login", "POST"), request -> {
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
            } catch (AuthenticationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }

            return handlerProcessingResult;
        });
    }
}
