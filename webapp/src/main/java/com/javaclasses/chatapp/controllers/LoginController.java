package com.javaclasses.chatapp.controllers;

import com.javaclasses.chatapp.AuthenticationException;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginController implements Handler{

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public TransferObject processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        final LoginDto loginDto = new LoginDto(username, password);

        TransferObject transferObject;

        try {
            TokenDto token = userService.login(loginDto);
            transferObject = new TransferObject(HttpServletResponse.SC_OK);
            transferObject.setData("token", String.valueOf(token.getToken()));
            transferObject.setData("userId", String.valueOf(token.getUserId().getId()));
            transferObject.setData("username", userService.findLoggedInUserByToken(token).getUsername());
        } catch (AuthenticationException e) {
            transferObject = new TransferObject(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            transferObject.setData("errorMessage", e.getMessage());
        }

        return transferObject;
    }
}
