package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

/**
 * DTO that is used while joining and leaving chat
 */
public class MemberChatDto {

    private final UserId userId;
    private final ChatId chatId;


    public MemberChatDto(UserId userId, ChatId chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public UserId getUserId() {
        return userId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberChatDto that = (MemberChatDto) o;
        return userId.equals(that.userId) && chatId.equals(that.chatId);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatId.hashCode();
        return result;
    }
}
