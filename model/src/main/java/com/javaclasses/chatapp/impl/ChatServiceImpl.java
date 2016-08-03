package com.javaclasses.chatapp.impl;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.*;
import com.javaclasses.chatapp.entities.Chat;
import com.javaclasses.chatapp.entities.Message;
import com.javaclasses.chatapp.storage.ChatRepositoryImpl;
import com.javaclasses.chatapp.storage.Repository;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.javaclasses.chatapp.ErrorType.*;

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
    public ChatId createChat(ChatCreationParametersDto chatCreationParametersDto) throws ChatCreationException {

        String chatName = chatCreationParametersDto.getChatName().trim();

        if (chatName.isEmpty()) {
            log.error("Failed to create chat: empty chat name");
            throw new ChatCreationException(CHATNAME_IS_EMPTY);
        }

        Collection<Chat> allChats = chatRepository.getAll();

        for (Chat chat : allChats) {

            if (chat.getChatName().equals(chatName)) {
                log.error("Failed to create chat: chat name {} is already taken", chatName);
                throw new ChatCreationException(DUPLICATE_CHATNAME);
            }
        }

        Chat newChat = new Chat(chatCreationParametersDto.getUserId(), chatName);
        ChatId newChatId = chatRepository.add(newChat);
        newChat.setChatId(newChatId);

        if (log.isInfoEnabled()) {
            log.info("Created chat {}", chatName);
        }

        return newChatId;
    }

    @Override
    public void addMember(MemberChatParametersDto memberChatParametersDto) throws MembershipException {

        Chat chat = chatRepository.getItem(memberChatParametersDto.getChatId());
        List<UserId> members = chat.getMembers();

        if (members.contains(memberChatParametersDto.getUserId())) {
            log.error("Failed to join chat: User {} is already a member", memberChatParametersDto.getUserId().getId());
            throw new MembershipException(ALREADY_A_MEMBER);
        } else {
            members.add(memberChatParametersDto.getUserId());
            chat.setMembers(members);

            if (log.isInfoEnabled()) {
                log.info("User {} has joined chat {}", memberChatParametersDto.getUserId().getId(), chat.getChatName());
            }
        }

    }

    @Override
    public void removeMember(MemberChatParametersDto memberChatParametersDto) throws MembershipException {

        Chat chat = chatRepository.getItem(memberChatParametersDto.getChatId());
        List<UserId> members = chat.getMembers();

        if (!members.contains(memberChatParametersDto.getUserId())) {
            log.error("Failed to leave chat: User {} is not a member", memberChatParametersDto.getUserId().getId());
            throw new MembershipException(IS_NOT_A_MEMBER);
        } else {
            members.remove(memberChatParametersDto.getUserId());
            chat.setMembers(members);

            if (log.isInfoEnabled()) {
                log.info("User {} has left chat {}", memberChatParametersDto.getUserId().getId(), chat.getChatName());
            }
        }
    }

    @Override
    public void postMessage(PostMessageParametersDto postMessageParametersDto) throws PostMessageException {

        Chat chat = chatRepository.getItem(postMessageParametersDto.getChatId());
        List<UserId> members = chat.getMembers();

        if (!members.contains(postMessageParametersDto.getUserId())) {
            log.error("Failed to post message: User {} is not a member", postMessageParametersDto.getUserId().getId());
            throw new PostMessageException(IS_NOT_A_MEMBER);
        } else {
            Message message = new Message(chat.getChatId(), postMessageParametersDto.getUsername(), postMessageParametersDto.getMessage());
            List<Message> messages = chat.getMessages();
            messages.add(message);
            chat.setMessages(messages);

            if (log.isInfoEnabled()) {
                log.info("User {} has posted a message to chat {}", postMessageParametersDto.getUserId().getId(), chat.getChatName());
            }
        }

    }

    @Override
    public ChatDto findChatById(ChatId id) {

        Chat chat = chatRepository.getItem(id);
        List<Message> messages = chat.getMessages();
        List<MessageDto> messageDtos = new ArrayList<>();

        for (Message message : messages) {
            messageDtos.add(new MessageDto(message.getAuthorName(), message.getChatId(), message.getMessage()));
        }
        return new ChatDto(chat.getChatId(), chat.getChatName(), chat.getOwnerId(), chat.getMembers(), messageDtos);
    }

    @Override
    public ChatDto findChatByName(String chatName) {

        final Collection<ChatDto> allChats = findAllChats();

        for (ChatDto chat : allChats) {
            if (chat.getChatName().equals(chatName)) {
                return chat;
            }
        }

        return null;
    }

    @Override
    public void deleteChat(ChatId chatId) {

        chatRepository.remove(chatId);

        if (log.isInfoEnabled()) {
            log.info("Removed chat {}", chatId.getId());
        }
    }

    @Override
    public Collection<ChatDto> findAllChats() {

        final Collection<Chat> chats = chatRepository.getAll();
        Collection<ChatDto> chatDtos = new ArrayList<>();

        for (Chat chat : chats) {
            List<Message> messages = chat.getMessages();
            List<MessageDto> messageDtos = new ArrayList<>();

            for (Message message : messages) {
                messageDtos.add(new MessageDto(message.getAuthorName(), message.getChatId(), message.getMessage()));
            }
            chatDtos.add(new ChatDto(chat.getChatId(), chat.getChatName(), chat.getOwnerId(), chat.getMembers(), messageDtos));
        }

        return chatDtos;
    }
}
