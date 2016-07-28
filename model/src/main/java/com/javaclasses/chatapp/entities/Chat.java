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
    private String chatName;
    private final UserId owner;
    private List<UserId> members;
    private List<Message> messages;

    public Chat(ChatId chatId, String chatName, UserId owner) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.owner = owner;

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

    public void setChatName(String chatName) {
        this.chatName = chatName;
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
