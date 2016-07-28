package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.ChatId;
import com.javaclasses.chatapp.UserId;

import java.util.List;

/**
 * Chat DTO
 */
public class ChatDto {

    private final ChatId chatId;
    private final UserId owner;
    private final List<UserId> members;
    private final List<String> messages;

    public ChatDto(ChatId chatId, UserId owner, List<UserId> members, List<String> messages) {
        this.chatId = chatId;
        this.owner = owner;
        this.members = members;
        this.messages = messages;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public UserId getOwner() {
        return owner;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatDto chatDto = (ChatDto) o;

        return chatId.equals(chatDto.chatId);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
