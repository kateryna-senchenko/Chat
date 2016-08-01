package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.HandlerProcessingResult;

import javax.servlet.http.HttpServletRequest;

public interface Handler {

    HandlerProcessingResult processRequest(HttpServletRequest request);
}
