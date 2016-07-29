package com.javaclasses.chatapp;


import com.javaclasses.chatapp.controllers.*;

import java.util.HashMap;
import java.util.Map;

/*package*/class HandlerRegistry {

    private static HandlerRegistry handlerRegistry = new HandlerRegistry();
    private final Map<CompoundKey, Handler> registry;

    private HandlerRegistry() {

        registry = new HashMap<CompoundKey, Handler>() {{
            put(new CompoundKey("/registration", "POST"), new RegistrationController());
            put(new CompoundKey("/login", "POST"), new LoginController());
            put(new CompoundKey("/createchat", "POST"), new ChatCreationController());
            put(new CompoundKey("/join-chat", "POST"), new JoiningChatController());
            put(new CompoundKey("/leave-chat", "POST"), new LeavingChatController());
            put(new CompoundKey("/post-message", "POST"), new PostingMessageController());
        }};
    }

    /*package*/static HandlerRegistry getInstance() {
        return handlerRegistry;
    }

    /*package*/Handler getHandler(CompoundKey key) {
        return registry.get(key);
    }
}
