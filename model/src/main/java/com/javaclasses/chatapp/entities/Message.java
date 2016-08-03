package com.javaclasses.chatapp.entities;

import com.javaclasses.chatapp.tinytypes.ChatId;

/**
 * Message entity
 */
public class Message {

    private final ChatId chatId;
    private final String authorName;
    private final String message;

    public Message(ChatId chatId, String authorName, String message) {

        this.authorName = authorName;
        this.chatId = chatId;
        this.message = message;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (!authorName.equals(message1.authorName)) return false;
        if (!chatId.equals(message1.chatId)) return false;
        return message != null ? message.equals(message1.message) : message1.message == null;

    }

    @Override
    public int hashCode() {
        int result = authorName.hashCode();
        result = 31 * result + chatId.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
