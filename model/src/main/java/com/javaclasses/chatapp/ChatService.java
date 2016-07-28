package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.ChatDto;
import com.javaclasses.chatapp.dto.MemberChatDto;
import com.javaclasses.chatapp.dto.PostMessageDto;

/**
 * Public API of ChatService
 */
public interface ChatService {

    /**
     * Creates new chat
     * @param chatCreationDto - contains owner userId and chat name
     * @return chat id
     * @throws ChatCreationException if chat creation fails
     */
    ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException;

    /**
     * Adds member to chat
     * @param memberChatDto - contains user id and chat id
     * @throws MembershipException if member cannot join the chat
     */
    void addMember(MemberChatDto memberChatDto) throws MembershipException;

    /**
     * Removes member from chat
     * @param memberChatDto - contains user id and chat id
     * @throws MembershipException if member cannot leave chat
     */
    void removeMember(MemberChatDto memberChatDto)throws MembershipException;

    /**
     * Posts a message
     * @param postMessageDto - contains user id, chat id and message
     * @throws PostMessageException if posting message fails
     */
    void postMessage(PostMessageDto postMessageDto) throws PostMessageException;

    /**
     * Finds chat by id
     * @param id - chat id
     * @return Chat DTO
     */
    ChatDto findChatById(ChatId id);
}
