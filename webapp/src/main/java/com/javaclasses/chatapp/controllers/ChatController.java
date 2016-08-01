package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.MemberChatDto;
import com.javaclasses.chatapp.dto.PostMessageDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.javaclasses.chatapp.Parameters.*;


public class ChatController {

    private static ChatController chatController = new ChatController();

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private ChatController(){
        createChat();
        addMemberToChat();
        removeMemberFromChat();
        postMassage();
    }

    public static ChatController getInstance() {
        return chatController;
    }

    private void createChat(){
        handlerRegistry.registerHandler(new CompoundKey("/create-chat", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final String userId = request.getParameter(USER_ID);
            final String chatName = request.getParameter(CHAT_NAME);

            final UserId id = new UserId(Long.valueOf(userId));
            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), id);
            HandlerProcessingResult handlerProcessingResult;
            if(userService.findLoggedInUserByToken(tokenDto) == null){
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");
            }

            final ChatCreationDto chatCreationDto = new ChatCreationDto(id, chatName);

            try {
                ChatId chatId = chatService.createChat(chatCreationDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(CHAT_NAME, chatService.findChatById(chatId).getChatName());
            } catch (ChatCreationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }

            return handlerProcessingResult;
        });
    }

    private void addMemberToChat(){
        handlerRegistry.registerHandler(new CompoundKey("/join-chat", "POST"), request -> {
            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));

            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if(userService.findLoggedInUserByToken(tokenDto) == null){
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");
            }

            final MemberChatDto memberChatDto = new MemberChatDto(userId, chatId);


            try {
                chatService.addMember(memberChatDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
            } catch (MembershipException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }
            return handlerProcessingResult;
        });
    }

    private void removeMemberFromChat(){
        handlerRegistry.registerHandler(new CompoundKey("/leave-chat", "POST"), request -> {
            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));

            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");
            }
            final MemberChatDto memberChatDto = new MemberChatDto(userId, chatId);

            try {
                chatService.removeMember(memberChatDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
            } catch (MembershipException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }
            return handlerProcessingResult;
        });
    }

    private void postMassage(){
        handlerRegistry.registerHandler(new CompoundKey("/post-message", "POST"), request -> {
            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));
            final String message = request.getParameter(CHAT_MESSAGE);

            final TokenDto tokenDto = new TokenDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");
            }

            final PostMessageDto postMessageDto = new PostMessageDto(userId, chatId, message);

            try {
                chatService.postMessage(postMessageDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(CHAT_MESSAGE, message);
            } catch (PostMessageException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());
            }

            return handlerProcessingResult;
        });
    }

}
