package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StringContent;

import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class ChatCreationController implements Handler{

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public TransferObject processRequest(HttpServletRequest request) {

        final String token = request.getParameter("token");
        final String userId = request.getParameter("userId");
        final String chatName = request.getParameter("chatName");

        final UserId id = new UserId(Long.valueOf(userId));
        final TokenDto tokenDto = new TokenDto(UUID.fromString(token), id);

        TransferObject transferObject;
        if(userService.findLoggedInUserByToken(tokenDto) == null){
            transferObject = new TransferObject(HttpServletResponse.SC_FORBIDDEN);
        }

        final ChatCreationDto chatCreationDto = new ChatCreationDto(id, chatName);

        try {
            ChatId chatId = chatService.createChat(chatCreationDto);
            transferObject = new TransferObject(HttpServletResponse.SC_OK);
            transferObject.setData("chatId", String.valueOf(chatId.getId()));
        } catch (ChatCreationException e) {
            transferObject = new TransferObject(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            transferObject.setData("message", e.getMessage());
        }

        return transferObject;
    }
}
