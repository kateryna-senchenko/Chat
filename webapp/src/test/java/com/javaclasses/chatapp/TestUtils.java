package com.javaclasses.chatapp;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.javaclasses.chatapp.Parameters.*;
import static org.apache.http.HttpHeaders.USER_AGENT;

/*package*/ class TestUtils {

    private final static HttpClient client = HttpClientBuilder.create().build();

    private final static String baseUrl = "http://localhost:8080";


    /*package*/
    static HttpResponse sendRegistrationRequest(String username, String password, String confirmPassword) throws IOException {

        final String registrationUrl = baseUrl + "/chat/registration";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USERNAME, username));
        parameters.add(new BasicNameValuePair(PASSWORD, password));
        parameters.add(new BasicNameValuePair(CONFIRM_PASSWORD, confirmPassword));

        return sendRequest(registrationUrl, parameters);
    }

    /*package*/
    static HttpResponse sendLoginRequest(String username, String password) throws IOException {

        final String loginUrl = baseUrl + "/chat/login";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(USERNAME, username));
        parameters.add(new BasicNameValuePair(PASSWORD, password));

        return sendRequest(loginUrl, parameters);
    }


    /*package*/
    static HttpResponse sendDeleteAccountRequest(String tokenId, String userId) throws IOException {

        final String deleteAccountUrl = baseUrl + "/chat/delete-account";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));

        return sendRequest(deleteAccountUrl, parameters);
    }


    /*package*/
    static HttpResponse sendCreationChatRequest(String tokenId, String userId, String chatName) throws IOException {

        final String createChatUrl = baseUrl + "/chat/create-chat";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_NAME, chatName));

        return sendRequest(createChatUrl, parameters);
    }

    /*package*/
    static HttpResponse sendDeleteChatRequest(String tokenId, String userId, String chatId) throws IOException {

        final String deleteChatUrl = baseUrl + "/chat/delete-chat";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));

        return sendRequest(deleteChatUrl, parameters);
    }


    /*package*/
    static HttpResponse sendJoiningChatRequest(String tokenId, String userId, String chatName) throws IOException {

        final String joinChatUrl = baseUrl + "/chat/join-chat";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_NAME, chatName));

        return sendRequest(joinChatUrl, parameters);
    }

    /*package*/
    static HttpResponse sendLeavingChatRequest(String tokenId, String userId, String chatId) throws IOException {

        final String leaveChatUrl = baseUrl + "/chat/leave-chat";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));

        return sendRequest(leaveChatUrl, parameters);
    }


    /*package*/
    static HttpResponse sendPostMessageRequest(String tokenId, String userId, String chatId, String message) throws IOException {

        final String postMessageUrl = baseUrl + "/chat/post-message";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair(TOKEN_ID, tokenId));
        parameters.add(new BasicNameValuePair(USER_ID, userId));
        parameters.add(new BasicNameValuePair(CHAT_ID, chatId));
        parameters.add(new BasicNameValuePair(CHAT_MESSAGE, message));

        return sendRequest(postMessageUrl, parameters);
    }

    /*package*/
    static JSONObject getResponseContent(HttpResponse response) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return new JSONObject(result.toString());
    }

    private static HttpResponse sendRequest(String url, List<NameValuePair> parameters) throws IOException {

        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        return client.execute(postRequest);
    }

}
