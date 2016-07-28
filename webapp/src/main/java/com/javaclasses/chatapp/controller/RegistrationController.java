package com.javaclasses.chatapp.controller;


import com.javaclasses.chatapp.RegistrationException;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.UserId;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.controller.Handler;
import com.javaclasses.chatapp.dto.RegistrationDTO;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;


public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public TransferObject processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, confirmPassword);

        TransferObject transferObject = new TransferObject();
        try {
            UserId id = userService.register(registrationDTO);
            transferObject.setContent(userService.findRegisteredUserById(id).getUsername());
        } catch (RegistrationException e) {
            transferObject.setContent(e.getMessage());
        }


        return transferObject;

    }
}
