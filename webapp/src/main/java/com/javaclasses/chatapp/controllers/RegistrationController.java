package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.RegistrationException;
import com.javaclasses.chatapp.HandlerProcessingResult;
import com.javaclasses.chatapp.tinytypes.UserId;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.javaclasses.chatapp.Parameters.*;


public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String username = request.getParameter(USERNAME.getName());
        final String password = request.getParameter(PASSWORD.getName());
        final String confirmPassword = request.getParameter(CONFIRM_PASSWORD.getName());

        final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        HandlerProcessingResult handlerProcessingResult;
        try {
            UserId userId = userService.register(registrationDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(USER_ID.getName(), String.valueOf(userId.getId()));
            handlerProcessingResult.setData(USERNAME.getName(), userService.findRegisteredUserById(userId).getUsername());
        } catch (RegistrationException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), e.getMessage());
        }

        return handlerProcessingResult;

    }
}
