package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.MemberChatDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class JoiningChatController implements Handler {

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();


    @Override
    public TransferObject processRequest(HttpServletRequest request) {

        final String token = request.getParameter("token");
        final UserId userId = new UserId(Long.valueOf(request.getParameter("userId")));
        final ChatId chatId = new ChatId(Long.valueOf(request.getParameter("chatId")));

        final TokenDto tokenDto = new TokenDto(UUID.fromString(token), userId);

        TransferObject transferObject;
        if(userService.findLoggedInUserByToken(tokenDto) == null){
            transferObject = new TransferObject(HttpServletResponse.SC_FORBIDDEN);
        }

        final MemberChatDto memberChatDto = new MemberChatDto(userId, chatId);


        try {
            chatService.addMember(memberChatDto);
            transferObject = new TransferObject(HttpServletResponse.SC_OK);
            transferObject.setData("userId", String.valueOf(userId.getId()));
            transferObject.setData("chatId", String.valueOf(chatId.getId()));
        } catch (MembershipException e) {
            transferObject = new TransferObject(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            transferObject.setData("errorMessage", e.getMessage());
        }
        return transferObject;
    }
}
