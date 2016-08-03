package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * Contains data to create a new chat
 */
public class ChatCreationDto {

    private final UserId userId;
    private final String chatName;


    public ChatCreationDto(UserId userId, String chatName) {
        this.userId = userId;
        this.chatName = chatName;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getChatName() {
        return chatName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatCreationDto chatCreationDto = (ChatCreationDto) o;

        return userId.equals(chatCreationDto.userId) && chatName.equals(chatCreationDto.chatName);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatName.hashCode();
        return result;
    }
}
