package com.javaclasses.chatapp;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javaclasses.chatapp.dto.RegistrationDTO;
import com.javaclasses.chatapp.dto.UserDTO;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RegistrationController implements Handler {

    private final UserService userService = UserServiceImpl.getInstance();

    public InputStream processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String confirmPassword = request.getParameter("confirmPassword");

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, confirmPassword);

        Object transferObject;
        try {
            UserId id = userService.register(registrationDTO);
            transferObject = userService.findRegisteredUserById(id);
        } catch (RegistrationException e) {
            transferObject = e.getMessage();
        }

        Gson gson = new GsonBuilder().create();

        return new ByteArrayInputStream(gson.toJson(transferObject).getBytes());

    }
}
