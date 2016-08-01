package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;


public class ChatCreationController implements Handler{

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String token = request.getParameter("token");
        final String userId = request.getParameter("userId");
        final String chatName = request.getParameter("chatName");

        final UserId id = new UserId(Long.valueOf(userId));
        final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(token)), id);
        HandlerProcessingResult handlerProcessingResult;
        if(userService.findLoggedInUserByToken(tokenDto) == null){
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
            handlerProcessingResult.setData("message", "Cannot find user");
        }

        final ChatCreationDto chatCreationDto = new ChatCreationDto(id, chatName);

        try {
            ChatId chatId = chatService.createChat(chatCreationDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData("chatId", String.valueOf(chatId.getId()));
            handlerProcessingResult.setData("chatName", chatService.findChatById(chatId).getChatName());
        } catch (ChatCreationException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData("errorMessage", e.getMessage());
        }

        return handlerProcessingResult;
    }
}
