package com.javaclasses.chatapp;


import com.javaclasses.chatapp.controller.Handler;
import com.javaclasses.chatapp.controller.LoginController;
import com.javaclasses.chatapp.controller.RegistrationController;

import java.util.HashMap;
import java.util.Map;

/* package */ class HandlerRegistry {

    private static HandlerRegistry handlerRegistry = new HandlerRegistry();
    private final Map<CompoundKey, Handler> registry;

    private HandlerRegistry() {

        registry = new HashMap<CompoundKey, Handler>() {{
            put(new CompoundKey("/registration", "POST"), new RegistrationController());
            put(new CompoundKey("/login", "POST"), new LoginController());
        }};
    }

    /* package */ static HandlerRegistry getInstance() {
        return handlerRegistry;
    }

    /* package */ Handler getHandler(CompoundKey key) {
        return registry.get(key);
    }
}
