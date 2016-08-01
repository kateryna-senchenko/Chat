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

import static com.javaclasses.chatapp.Parameters.*;


public class ChatCreationController implements Handler{

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String tokenId = request.getParameter(TOKEN_ID.getName());
        final String userId = request.getParameter(USER_ID.getName());
        final String chatName = request.getParameter(CHAT_NAME.getName());

        final UserId id = new UserId(Long.valueOf(userId));
        final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), id);
        HandlerProcessingResult handlerProcessingResult;
        if(userService.findLoggedInUserByToken(tokenDto) == null){
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), "Cannot find user");
        }

        final ChatCreationDto chatCreationDto = new ChatCreationDto(id, chatName);

        try {
            ChatId chatId = chatService.createChat(chatCreationDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(CHAT_ID.getName(), String.valueOf(chatId.getId()));
            handlerProcessingResult.setData(CHAT_NAME.getName(), chatService.findChatById(chatId).getChatName());
        } catch (ChatCreationException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), e.getMessage());
        }

        return handlerProcessingResult;
    }
}
