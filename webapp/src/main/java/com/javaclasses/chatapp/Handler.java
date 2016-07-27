package com.javaclasses.chatapp;


import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;


public interface Handler {

    InputStream processRequest(HttpServletRequest request);
}
