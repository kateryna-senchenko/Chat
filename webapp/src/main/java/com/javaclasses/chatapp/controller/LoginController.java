package com.javaclasses.chatapp.controller;

import com.javaclasses.chatapp.AuthenticationException;
import com.javaclasses.chatapp.Token;
import com.javaclasses.chatapp.TransferObject;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.LoginDTO;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

public class LoginController implements Handler{

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public TransferObject processRequest(HttpServletRequest request) {

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        LoginDTO loginDTO = new LoginDTO(username, password);

        TransferObject transferObject = new TransferObject();

        try {
            Token token = userService.login(loginDTO);
            transferObject.setContent(userService.findLoggedInUserByToken(token).getUsername());
        } catch (AuthenticationException e) {
            transferObject.setContent(e.getMessage());
        }


        return transferObject;
    }
}
