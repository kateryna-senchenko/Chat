package com.javaclasses.chatapp;

import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.javaclasses.chatapp.ErrorType.DUPLICATE_CHATNAME;
import static com.javaclasses.chatapp.Parameters.*;
import static com.javaclasses.chatapp.Parameters.CHAT_ID;
import static com.javaclasses.chatapp.TestUtils.*;
import static org.junit.Assert.assertEquals;

public class ChatControllerShould {

    private String tokenId;
    private String userId;

    private  final String chatName = "Go!";


    @Before
    public void registerAndLoginUser() throws IOException {

        final String username = "Balto";
        final String password = "thecallofthewild";

        sendRegistrationRequest(username, password, password);

        final HttpResponse loginResponse = sendLoginRequest(username, password);

        final JSONObject loginResult = getResponseContent(loginResponse);

        tokenId = loginResult.optString(TOKEN_ID);
        userId = loginResult.optString(USER_ID);

    }

    @After
    public void deleteAccount() throws IOException {
        sendDeleteAccountRequest(tokenId, userId);
    }

    @Test
    public void createChat() throws IOException {

        final HttpResponse chatCreationResponse =
                sendCreationChatRequest(tokenId, userId, chatName);

        final JSONObject chatCreationResult = getResponseContent(chatCreationResponse);

        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, chatCreationResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", chatName, chatCreationResult.optString(CHAT_NAME));

        sendDeleteChatRequest(tokenId, userId, chatCreationResult.optString(CHAT_ID));

    }

    @Test
    public void failToCreateChatWithDuplicateChatNames() throws IOException {

        final HttpResponse firstChatCreationResponse =
                sendCreationChatRequest(tokenId, userId, chatName);

        final JSONObject firstChatCreationResult = getResponseContent(firstChatCreationResponse);

        final HttpResponse secondChatCreationResponse =
                sendCreationChatRequest(tokenId, userId, chatName);

        final JSONObject secondChatCreationResult = getResponseContent(secondChatCreationResponse);

        final int expectedStatus = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        final String expectedMessage = DUPLICATE_CHATNAME.getMessage();

        assertEquals("Unexpected response status", expectedStatus,
                secondChatCreationResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, secondChatCreationResult.optString(ERROR_MESSAGE));

        sendDeleteChatRequest(tokenId, userId, firstChatCreationResult.optString(CHAT_ID));

    }

    @Test
    public void addMemberToChat() throws IOException {

        final HttpResponse chatCreationResponse = sendCreationChatRequest(tokenId, userId, chatName);
        final JSONObject chatCreationResult = getResponseContent(chatCreationResponse);

        final HttpResponse joiningChatResponse =
                sendJoiningChatRequest(tokenId, userId, chatName);

        final JSONObject joiningChatResult = getResponseContent(joiningChatResponse);

        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, joiningChatResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", chatName, joiningChatResult.optString(CHAT_NAME));

        sendDeleteChatRequest(tokenId, userId, chatCreationResult.optString(CHAT_ID));

    }

    @Test
    public void removeMemberFromChat() throws IOException {

        final HttpResponse chatCreationResponse = sendCreationChatRequest(tokenId, userId, chatName);
        final JSONObject chatCreationResult = getResponseContent(chatCreationResponse);

        sendJoiningChatRequest(tokenId, userId, chatName);

        final HttpResponse leavingChatResponse =
                sendLeavingChatRequest(tokenId, userId, chatCreationResult.optString(CHAT_ID));

        final JSONObject leavingChatResult = getResponseContent(leavingChatResponse);

        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, leavingChatResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", chatName, leavingChatResult.optString(CHAT_NAME));

        sendDeleteChatRequest(tokenId, userId, chatCreationResult.optString(CHAT_ID));

    }

    @Test
    public void postMessage() throws IOException {


        final HttpResponse chatCreationResponse =
                sendCreationChatRequest(tokenId, userId, chatName);
        final JSONObject chatCreationResult = getResponseContent(chatCreationResponse);

        sendJoiningChatRequest(tokenId, userId, chatName);

        final String message = "Hello there!";
        final HttpResponse postingMessageResponse =
                sendPostMessageRequest(tokenId, userId , chatCreationResult.optString(CHAT_ID), message);

        final JSONObject postingMessageResult = getResponseContent(postingMessageResponse);

        final int expectedStatus = HttpServletResponse.SC_OK;

        assertEquals("Unexpected response status", expectedStatus, postingMessageResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", message, postingMessageResult.optString(CHAT_MESSAGE));

        sendDeleteChatRequest(tokenId, userId, chatCreationResult.optString(CHAT_ID));

    }

}
