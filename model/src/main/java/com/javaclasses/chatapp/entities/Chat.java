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
    private String chatName;
    private final UserId owner;
    private List<UserId> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public Chat(UserId owner, String chatName) {
        this.chatName = chatName;
        this.owner = owner;

    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
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

    public void setChatId(ChatId chatId) {
        this.chatId = chatId;
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
