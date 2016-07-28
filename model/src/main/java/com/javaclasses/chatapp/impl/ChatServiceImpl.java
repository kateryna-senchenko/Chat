package com.javaclasses.chatapp.impl;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.ChatDto;
import com.javaclasses.chatapp.dto.MemberChatDto;
import com.javaclasses.chatapp.dto.PostMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the ChatService  interface
 */
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private static ChatService chatService = new ChatServiceImpl();

    private ChatServiceImpl() {
    }

    public static ChatService getInstance() {
        return chatService;
    }


    @Override
    public ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException {
        return null;
    }

    @Override
    public void addMember(MemberChatDto memberChatDto) throws MembershipException {

    }

    @Override
    public void removeMember(MemberChatDto memberChatDto) throws MembershipException {

    }

    @Override
    public void postMessage(PostMessageDto postMessageDto) throws PostMessageException {

    }

    @Override
    public ChatDto findChatById(ChatId id) {
        return null;
    }
}
