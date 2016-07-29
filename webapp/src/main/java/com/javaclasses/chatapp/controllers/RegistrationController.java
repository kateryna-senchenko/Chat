package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.RegistrationException;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.tinytypes.UserId;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public TransferObject processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        TransferObject transferObject;
        try {
            UserId userId = userService.register(registrationDto);
            transferObject = new TransferObject(HttpServletResponse.SC_OK);
            transferObject.setData("userId", String.valueOf(userId.getId()));
            transferObject.setData("username", userService.findRegisteredUserById(userId).getUsername());
        } catch (RegistrationException e) {
            transferObject = new TransferObject(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            transferObject.setData("errorMessage", e.getMessage());
        }

        return transferObject;

    }
}
