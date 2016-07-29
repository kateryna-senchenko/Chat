package com.javaclasses.chatapp.storage;

import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.entities.Chat;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementation of the Repository interface for Chat repository
 */
public class ChatRepositoryImpl extends InMemoryRepository<ChatId, Chat>{

    private AtomicLong count = new AtomicLong(0);

    private static Repository<ChatId, Chat> chatRepository = new ChatRepositoryImpl();

    private ChatRepositoryImpl(){}

    public static Repository<ChatId, Chat> getInstance(){
        return chatRepository;
    }

    @Override
    public ChatId generateId() {
        return new ChatId(count.getAndIncrement());
    }
}
