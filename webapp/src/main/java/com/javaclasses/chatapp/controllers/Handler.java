package com.javaclasses.chatapp.controllers;


import com.javaclasses.chatapp.TransferObject;

import javax.servlet.http.HttpServletRequest;

public interface Handler {

    TransferObject processRequest(HttpServletRequest request);
}