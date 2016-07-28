package com.javaclasses.chatapp;

import com.javaclasses.chatapp.controller.Handler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DispatcherServlet extends HttpServlet {


    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        final CompoundKey key = new CompoundKey(uri, method);

        final Handler handler = handlerRegistry.getHandler(key);

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            TransferObject transferObject = handler.processRequest(request, response);
            response.getWriter().write(transferObject.getContent());
        }
    }

}