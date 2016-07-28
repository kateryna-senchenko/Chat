package com.javaclasses.chatapp.controller;


import com.javaclasses.chatapp.TransferObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface Handler {

    TransferObject processRequest(HttpServletRequest request, HttpServletResponse response);
}
