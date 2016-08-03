package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * Contains data to create a new chat
 */
public class ChatCreationParametersDto {

    private final UserId userId;
    private final String chatName;


    public ChatCreationParametersDto(UserId userId, String chatName) {
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

        ChatCreationParametersDto chatCreationParametersDto = (ChatCreationParametersDto) o;

        return userId.equals(chatCreationParametersDto.userId) && chatName.equals(chatCreationParametersDto.chatName);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatName.hashCode();
        return result;
    }
}
