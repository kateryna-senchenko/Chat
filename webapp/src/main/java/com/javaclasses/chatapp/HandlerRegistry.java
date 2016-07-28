package com.javaclasses.chatapp;


import com.javaclasses.chatapp.controller.Handler;
import com.javaclasses.chatapp.controller.RegistrationController;

import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {

    private static HandlerRegistry handlerRegistry = new HandlerRegistry();
    private final Map<String, Handler> registry;

    private HandlerRegistry() {

         registry = new HashMap<String, Handler>() {{
            put("/registrationPOST", new RegistrationController());
    }};
    }

    public static HandlerRegistry getInstance() {
        return handlerRegistry;
    }

    public Handler getHandler(String key){
        return registry.get(key);
    }
}
