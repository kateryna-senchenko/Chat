package com.javaclasses.chatapp.controllers;

import com.javaclasses.chatapp.AuthenticationException;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.HandlerProcessingResult;
import com.javaclasses.chatapp.UserService;
import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.javaclasses.chatapp.Parameters.*;

public class LoginController implements Handler{

    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String username = request.getParameter(USERNAME.getName());
        final String password = request.getParameter(PASSWORD.getName());

        final LoginDto loginDto = new LoginDto(username, password);

        HandlerProcessingResult handlerProcessingResult;

        try {
            TokenDto token = userService.login(loginDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(TOKEN_ID.getName(), String.valueOf(token.getToken().getId()));
            handlerProcessingResult.setData(USER_ID.getName(), String.valueOf(token.getUserId().getId()));
            handlerProcessingResult.setData(USERNAME.getName(), userService.findLoggedInUserByToken(token).getUsername());
        } catch (AuthenticationException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), e.getMessage());
        }

        return handlerProcessingResult;
    }
}
