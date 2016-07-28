package com.javaclasses.chatapp;

/**
 * Object to hold message data
 */
public class Message {

    private final UserId author;
    private final ChatId chatId;
    private final String message;

    public Message(UserId author, ChatId chatId, String message) {
        this.author = author;
        this.chatId = chatId;
        this.message = message;
    }

    public ChatId getChatId() {
        return chatId;
    }

    public UserId getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        return chatId.equals(message1.chatId) && author.equals(message1.author) &&
                (message != null ? message.equals(message1.message) : message1.message == null);

    }

    @Override
    public int hashCode() {
        int result = chatId.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
