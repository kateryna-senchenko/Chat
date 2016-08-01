package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.RegistrationException;
import com.javaclasses.chatapp.HandlerProcessingResult;
import com.javaclasses.chatapp.tinytypes.UserId;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        HandlerProcessingResult handlerProcessingResult;
        try {
            UserId userId = userService.register(registrationDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData("userId", String.valueOf(userId.getId()));
            handlerProcessingResult.setData("username", userService.findRegisteredUserById(userId).getUsername());
        } catch (RegistrationException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData("errorMessage", e.getMessage());
        }

        return handlerProcessingResult;

    }
}
