package com.javaclasses.chatapp.controller;


import com.javaclasses.chatapp.RegistrationException;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.UserId;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public TransferObject processRequest(HttpServletRequest request, HttpServletResponse response) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        TransferObject transferObject;
        try {
            UserId id = userService.register(registrationDto);
            transferObject = new TransferObject(userService.findRegisteredUserById(id).getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (RegistrationException e) {
            transferObject = new TransferObject(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return transferObject;

    }
}
