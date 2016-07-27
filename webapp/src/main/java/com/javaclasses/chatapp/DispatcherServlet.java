package com.javaclasses.chatapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "DispatcherServlet")
public class DispatcherServlet extends HttpServlet {


    private HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        final String uri = request.getRequestURI();
        final String type = request.getMethod();

        handlerRegistry.getHandler(uri + type).processRequest(request);

        
    }
}