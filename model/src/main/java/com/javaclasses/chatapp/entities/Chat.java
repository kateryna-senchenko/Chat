package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.ChatId;
import com.javaclasses.chatapp.Message;
import com.javaclasses.chatapp.UserId;

import java.util.List;

/**
 * Chat entity
 */
public class Chat {

    private final ChatId chatId;
    private final UserId owner;
    private final List<UserId> members;
    private final List<Message> messages;

    public Chat(ChatId chatId, UserId owner, List<UserId> members, List<Message> messages) {
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

    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chat chat = (Chat) o;

        return chatId.equals(chat.chatId);

    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}
