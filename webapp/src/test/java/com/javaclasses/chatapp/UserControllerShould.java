package com.javaclasses.chatapp;


import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.javaclasses.chatapp.ErrorType.AUTHENTICATION_FAILED;
import static com.javaclasses.chatapp.ErrorType.DUPLICATE_USERNAME;
import static com.javaclasses.chatapp.Parameters.*;
import static com.javaclasses.chatapp.TestUtils.*;
import static com.javaclasses.chatapp.TestUtils.getResponseContent;
import static com.javaclasses.chatapp.TestUtils.sendLoginRequest;
import static org.junit.Assert.assertEquals;

public class UserControllerShould {

    private final String username = "Balto";
    private final String password = "thecallofthewild";

    @Test
    public void registerUser() throws IOException {

        final HttpResponse postResponse = sendRegistrationRequest(username, password, password);
        final JSONObject jsonResult = getResponseContent(postResponse);

        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", username, jsonResult.optString(USERNAME));

        final HttpResponse loginResponse = sendLoginRequest(username, password);
        final JSONObject loginResult = getResponseContent(loginResponse);
        sendDeleteAccountRequest(loginResult.optString(TOKEN_ID), loginResult.optString(USER_ID));

    }

    @Test
    public void failToRegisterUserWithDuplicateUsername() throws IOException {

        sendRegistrationRequest(username, password, password);

        final HttpResponse secondPostResponse = sendRegistrationRequest(username, password, password);
        final JSONObject secondResult = getResponseContent(secondPostResponse);

        final int expectedStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        final String expectedMessage = DUPLICATE_USERNAME.getMessage();

        assertEquals("Unexpected response status", expectedStatus, secondPostResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, secondResult.optString(ERROR_MESSAGE));

        final HttpResponse loginResponse = sendLoginRequest(username, password);
        final JSONObject loginResult = getResponseContent(loginResponse);
        sendDeleteAccountRequest(loginResult.optString(TOKEN_ID), loginResult.optString(USER_ID));

    }

    @Test
    public void loginUser() throws IOException {

        sendRegistrationRequest(username, password, password);

        final HttpResponse postResponse = sendLoginRequest(username, password);

        final JSONObject jsonResult = getResponseContent(postResponse);
        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", username, jsonResult.optString(USERNAME));

        sendDeleteAccountRequest(jsonResult.optString(TOKEN_ID), jsonResult.optString(USER_ID));

    }

    @Test
    public void failToLoginUnregisteredUser() throws IOException {

        final HttpResponse postResponse = sendLoginRequest(username, password);

        final JSONObject jsonResult = getResponseContent(postResponse);
        final int expectedStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        final String expectedMessage = AUTHENTICATION_FAILED.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, jsonResult.optString(ERROR_MESSAGE));
    }
}
