package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * Message DTO
 */
public class MessageDto {

    private final ChatId chatId;
    private final UserId author;
    private final String authorName;
    private final String message;

    public MessageDto(String authorName, UserId author, ChatId chatId, String message) {
        this.authorName = authorName;
        this.author = author;
        this.chatId = chatId;
        this.message = message;
    }

    public String getAuthorName() {
        return authorName;
    }

    public UserId getAuthor() {
        return author;
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

        MessageDto that = (MessageDto) o;

        return authorName.equals(that.authorName) && chatId.equals(that.chatId) && message.equals(that.message);

    }

    @Override
    public int hashCode() {
        int result = authorName.hashCode();
        result = 31 * result + chatId.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
