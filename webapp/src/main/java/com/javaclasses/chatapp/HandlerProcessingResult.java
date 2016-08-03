package com.javaclasses.chatapp;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HandlerProcessingResult {

    private final int responseStatus;
    private final Map<String, String> content;

    public HandlerProcessingResult(int responseStatus) {
        this.responseStatus = responseStatus;
        this.content = new HashMap<>();
    }


    public void setData(String key, String value){
        content.put(key, value);
    }

    /*package*/ int getResponseStatus(){
        return responseStatus;
    }

    /*package*/ String getContent(){
        return new JSONObject(content).toString();
    }
}
