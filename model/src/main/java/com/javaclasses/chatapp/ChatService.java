package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.ChatCreationParametersDto;
import com.javaclasses.chatapp.dto.ChatEntityDto;
import com.javaclasses.chatapp.dto.MemberChatParametersDto;
import com.javaclasses.chatapp.dto.PostMessageParametersDto;
import com.javaclasses.chatapp.tinytypes.ChatId;

import java.util.Collection;

/**
 * Public API of ChatService
 */
public interface ChatService {

    /**
     * Creates new chat
     * @param chatCreationParametersDto - contains owner userId and chat name
     * @return chat id
     * @throws ChatCreationException if chat creation fails
     */
    ChatId createChat(ChatCreationParametersDto chatCreationParametersDto) throws ChatCreationException;

    /**
     * Adds member to chat
     * @param memberChatParametersDto - contains user id and chat id
     * @throws MembershipException if member cannot join the chat
     */
    void addMember(MemberChatParametersDto memberChatParametersDto) throws MembershipException;

    /**
     * Removes member from chat
     * @param memberChatParametersDto - contains user id and chat id
     * @throws MembershipException if member cannot leave chat
     */
    void removeMember(MemberChatParametersDto memberChatParametersDto)throws MembershipException;

    /**
     * Posts a message
     * @param postMessageParametersDto - contains user id, chat id and message
     * @throws PostMessageException if posting message fails
     */
    void postMessage(PostMessageParametersDto postMessageParametersDto) throws PostMessageException;

    /**
     * Provides access to chat by id
     * @param id - chat id
     * @return Chat DTO
     */
    ChatEntityDto findChatById(ChatId id);

    /**
     * Provides access to chat by name
     * @param chatName - chat name
     * @return Chat DTO
     */
    ChatEntityDto findChatByName(String chatName);

    /**
     * Removes chad with specified id
     * @param chatId - id of the chat to be removed
     */
    void deleteChat(ChatId chatId);

    /**
     * Provides access to collection of all chats
     * @return collection of chat dto
     */
    Collection<ChatEntityDto> findAllChats();

}
