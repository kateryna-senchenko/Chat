package com.javaclasses.chatapp.controller;

import com.javaclasses.chatapp.AuthenticationException;
import com.javaclasses.chatapp.entities.Token;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Handler{

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public TransferObject processRequest(HttpServletRequest request, HttpServletResponse response) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        final LoginDto loginDto = new LoginDto(username, password);

        TransferObject transferObject;

        try {
            Token token = userService.login(loginDto);
            transferObject = new TransferObject(userService.findLoggedInUserByToken(token).getUsername());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (AuthenticationException e) {
            transferObject = new TransferObject(e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        return transferObject;
    }
}
