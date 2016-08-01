package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.PostMessageDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class PostingMessageController implements Handler {

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String token = request.getParameter("token");
        final UserId userId = new UserId(Long.valueOf(request.getParameter("userId")));
        final ChatId chatId = new ChatId(Long.valueOf(request.getParameter("chatId")));
        final String message = request.getParameter("message");

        final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(token)), userId);

        HandlerProcessingResult handlerProcessingResult;
        if (userService.findLoggedInUserByToken(tokenDto) == null) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
        }

        final PostMessageDto postMessageDto = new PostMessageDto(userId, chatId, message);

        try {
            chatService.postMessage(postMessageDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData("userId", String.valueOf(userId.getId()));
            handlerProcessingResult.setData("chatId", String.valueOf(chatId.getId()));
            handlerProcessingResult.setData("message", message);
        } catch (PostMessageException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData("errorMessage", e.getMessage());
        }

        return handlerProcessingResult;
    }
}
