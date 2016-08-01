package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * Message DTO
 */
public class MessageDto {

    private final UserId author;
    private final ChatId chatId;
    private final String message;

    public MessageDto(UserId author, ChatId chatId, String message) {
        this.author = author;
        this.chatId = chatId;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageDto that = (MessageDto) o;

        return author.equals(that.author) && chatId.equals(that.chatId) && message.equals(that.message);

    }

    @Override
    public int hashCode() {
        int result = author.hashCode();
        result = 31 * result + chatId.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
