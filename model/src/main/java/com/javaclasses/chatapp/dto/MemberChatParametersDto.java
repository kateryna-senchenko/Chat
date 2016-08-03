package com.javaclasses.chatapp.dto;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * Contains data necessary to join or leave chat
 */
public class MemberChatParametersDto {

    private final UserId userId;
    private final ChatId chatId;


    public MemberChatParametersDto(UserId userId, ChatId chatId) {
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

        MemberChatParametersDto that = (MemberChatParametersDto) o;
        return userId.equals(that.userId) && chatId.equals(that.chatId);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + chatId.hashCode();
        return result;
    }
}
