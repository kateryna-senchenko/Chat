package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat entity
 */
public class Chat {

    private ChatId chatId;
    private final String chatName;
    private final UserId ownerId;
    private List<UserId> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Chat(UserId ownerId, String chatName) {
        this.chatName = chatName;
        this.ownerId = ownerId;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public UserId getOwnerId() {
        return ownerId;
    }

    public List<UserId> getMembers() {
        return members;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
    }

    public void setMembers(List<UserId> members) {
        this.members = members;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
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
