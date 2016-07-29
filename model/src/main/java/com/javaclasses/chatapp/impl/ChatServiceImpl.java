package com.javaclasses.chatapp.impl;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.ChatCreationDto;
import com.javaclasses.chatapp.dto.ChatDto;
import com.javaclasses.chatapp.dto.MemberChatDto;
import com.javaclasses.chatapp.dto.PostMessageDto;
import com.javaclasses.chatapp.entities.Chat;
import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.storage.ChatRepositoryImpl;
import com.javaclasses.chatapp.storage.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of the ChatService  interface
 */
public class ChatServiceImpl implements ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);

    private static ChatService chatService = new ChatServiceImpl();
    private static Repository<ChatId, Chat> chatRepository;

    private ChatServiceImpl() {
        chatRepository = ChatRepositoryImpl.getInstance();
    }

    public static ChatService getInstance() {
        return chatService;
    }


    @Override
    public ChatId createChat(ChatCreationDto chatCreationDto) throws ChatCreationException {

        String chatName = chatCreationDto.getChatName().trim();

        if (chatName.isEmpty()) {
            log.error("Failed to create chat: empty chat name");
            throw new ChatCreationException("Chat name should not be empty");
        }

        Collection<Chat> allChats = chatRepository.getAll();

        for (Chat chat : allChats) {

            if (chat.getChatName().equals(chatName)) {
                log.error("Failed to create chat: chat name {} is already taken", chatName);
                throw new ChatCreationException("Specified name is not available");
            }
        }

        Chat newChat = new Chat(chatCreationDto.getUserId(), chatName);
        ChatId newChatId = chatRepository.add(newChat);
        newChat.setChatId(newChatId);

        if (log.isInfoEnabled()) {
            log.info("Created chat {}", chatName);
        }

        return newChatId;
    }

    @Override
    public void addMember(MemberChatDto memberChatDto) throws MembershipException {

        Chat chat = chatRepository.getItem(memberChatDto.getChatId());
        List<UserId> members = chat.getMembers();

        if(members.contains(memberChatDto.getUserId())){
            log.error("Failed to join chat: User {} is already a member", memberChatDto.getUserId().getId());
            throw new MembershipException("User " + memberChatDto.getUserId().getId() + " is already a member");
        }else{
            members.add(memberChatDto.getUserId());
            chat.setMembers(members);

            if (log.isInfoEnabled()) {
                log.info("User {} has joined chat {}", memberChatDto.getUserId().getId(), chat.getChatName());
            }
        }

    }

    @Override
    public void removeMember(MemberChatDto memberChatDto) throws MembershipException {

        Chat chat = chatRepository.getItem(memberChatDto.getChatId());
        List<UserId> members = chat.getMembers();

        if(!members.contains(memberChatDto.getUserId())){
            log.error("Failed to leave chat: User {} is not a member", memberChatDto.getUserId().getId());
            throw new MembershipException("User " + memberChatDto.getUserId().getId() + " is not a member");
        }else{
            members.remove(memberChatDto.getUserId());
            chat.setMembers(members);

            if (log.isInfoEnabled()) {
                log.info("User {} has left chat {}", memberChatDto.getUserId().getId(), chat.getChatName());
            }
        }
    }

    @Override
    public void postMessage(PostMessageDto postMessageDto) throws PostMessageException {

        Chat chat = chatRepository.getItem(postMessageDto.getChatId());
        List<UserId> members = chat.getMembers();

        if(!members.contains(postMessageDto.getUserId())){
            log.error("Failed to post message: User {} is not a member", postMessageDto.getUserId().getId());
            throw new PostMessageException("Cannot post message if not a member");
        }else{
            Message message = new Message(postMessageDto.getUserId(), chat.getChatId(), postMessageDto.getMessage());
            List<Message> messages = chat.getMessages();
            messages.add(message);
            chat.setMessages(messages);

            if (log.isInfoEnabled()) {
                log.info("User {} has posted a message to chat {}", postMessageDto.getUserId().getId(), chat.getChatName());
            }
        }

    }

    @Override
    public ChatDto findChatById(ChatId id) {

        Chat chat = chatRepository.getItem(id);
        return new ChatDto(chat.getChatId(), chat.getChatName(), chat.getOwner(), chat.getMembers(), chat.getMessages());
    }

    @Override
    public void deleteAll() {
         chatRepository.deleteAll();

        if (log.isInfoEnabled()) {
            log.info("Chat repository has been cleared");
        }
    }
}
