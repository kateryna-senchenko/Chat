package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.ChatId;
import com.javaclasses.chatapp.UserId;

/**
 * DTO that is used while posting a message
 */
public class PostMessageDto {

    private final UserId userId;
    private final ChatId chatId;
    private final String message;

    public PostMessageDto(UserId userId, ChatId chatId, String message) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
    }

    public UserId getUserId() {
        return userId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostMessageDto that = (PostMessageDto) o;
        return userId.equals(that.userId) && chatId.equals(that.chatId) && message.equals(that.message);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatId.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
