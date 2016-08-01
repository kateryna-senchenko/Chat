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

import static com.javaclasses.chatapp.Parameters.*;

public class PostingMessageController implements Handler {

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    public HandlerProcessingResult processRequest(HttpServletRequest request) {

        final String tokenId = request.getParameter(TOKEN_ID.getName());
        final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID.getName())));
        final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID.getName())));
        final String message = request.getParameter(CHAT_MESSAGE.getName());

        final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), userId);

        HandlerProcessingResult handlerProcessingResult;
        if (userService.findLoggedInUserByToken(tokenDto) == null) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), "Cannot find user");
        }

        final PostMessageDto postMessageDto = new PostMessageDto(userId, chatId, message);

        try {
            chatService.postMessage(postMessageDto);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(USER_ID.getName(), String.valueOf(userId.getId()));
            handlerProcessingResult.setData(CHAT_ID.getName(), String.valueOf(chatId.getId()));
            handlerProcessingResult.setData(CHAT_MESSAGE.getName(), message);
        } catch (PostMessageException e) {
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            handlerProcessingResult.setData(ERROR_MESSAGE.getName(), e.getMessage());
        }

        return handlerProcessingResult;
    }
}
