package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

import java.util.List;


/**
 * Chat DTO
 */
public class ChatEntityDto {

    private final ChatId chatId;
    private final String chatName;
    private final UserId owner;
    private final List<UserId> members;
    private final List<MessageEntityDto> messages;

    public ChatEntityDto(ChatId chatId, String chatName, UserId owner, List<UserId> members, List<MessageEntityDto> messages) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.owner = owner;
        this.members = members;
        this.messages = messages;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public UserId getOwner() {
        return owner;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<MessageEntityDto> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatEntityDto chatEntityDto = (ChatEntityDto) o;

        return chatId.equals(chatEntityDto.chatId);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
