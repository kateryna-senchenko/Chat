package com.javaclasses.chatapp;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.javaclasses.chatapp.ErrorType.AUTHENTICATION_FAILED;
import static com.javaclasses.chatapp.ErrorType.DUPLICATE_CHATNAME;
import static com.javaclasses.chatapp.ErrorType.DUPLICATE_USERNAME;
import static org.apache.http.HttpHeaders.USER_AGENT;
import static org.junit.Assert.assertEquals;


public class DispatcherServletShould {

    private final HttpClient client = HttpClientBuilder.create().build();

    private HttpResponse sendRequest(String url, List<NameValuePair>parameters) throws IOException {
        HttpPost postRequest = new HttpPost(url);
        postRequest.setHeader("User-Agent", USER_AGENT);

        postRequest.setEntity(new UrlEncodedFormEntity(parameters));

        return client.execute(postRequest);
    }

    private JSONObject getResponseContent(HttpResponse response) throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return new JSONObject(result.toString());
    }



    @Test
    public void acceptSuccessfulRegistrationRequest() throws IOException {

        final String url = "http://localhost:8080/registration";

        final String username = "Balto";
        final String password = "thecallofthewild";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("confirmPassword", password));

        HttpResponse postResponse = sendRequest(url, parameters);
        final JSONObject jsonResult = getResponseContent(postResponse);

        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", username, jsonResult.optString("username"));

    }

    @Test
    public void acceptRegistrationRequestWithDuplicateUsername() throws IOException {

        final String url = "http://localhost:8080/registration";

        final String username = "Alue";
        final String password = "thecallofthewild";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));
        parameters.add(new BasicNameValuePair("confirmPassword", password));

        sendRequest(url, parameters);
        HttpResponse postResponse = sendRequest(url, parameters);

        final JSONObject jsonResult = getResponseContent(postResponse);
        final int expectedStatus = 500;
        final String expectedMessage = DUPLICATE_USERNAME.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, jsonResult.optString("errorMessage"));

    }

    @Test
    public void acceptSuccessfulLoginRequest() throws IOException {

        final String registrationUrl = "http://localhost:8080/registration";

        final String username = "BooReadley";
        final String password = "boo";

        final List<NameValuePair> registrationParameters = new ArrayList<>();
        registrationParameters.add(new BasicNameValuePair("username", username));
        registrationParameters.add(new BasicNameValuePair("password", password));
        registrationParameters.add(new BasicNameValuePair("confirmPassword", password));

        sendRequest(registrationUrl, registrationParameters);

        final String loginUrl = "http://localhost:8080/login";

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("username", username));
        loginParameters.add(new BasicNameValuePair("password", password));

        HttpResponse postResponse = sendRequest(loginUrl, loginParameters);

        final JSONObject jsonResult = getResponseContent(postResponse);
        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", username, jsonResult.optString("username"));

    }

    @Test
    public void acceptLoginRequestFromUnregisteredUser() throws IOException {

        final String url = "http://localhost:8080/login";

        final String username = "AtticusFinch";
        final String password = "ForGod'sSakeBelieveHim";

        final List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("username", username));
        parameters.add(new BasicNameValuePair("password", password));

        HttpResponse postResponse = sendRequest(url, parameters);

        final JSONObject jsonResult = getResponseContent(postResponse);
        final int expectedStatus = 500;
        final String expectedMessage = AUTHENTICATION_FAILED.getMessage();

        assertEquals("Unexpected response status", expectedStatus, postResponse.getStatusLine().getStatusCode());
        assertEquals("Post request failed", expectedMessage, jsonResult.optString("errorMessage"));
    }

    @Test
    public void acceptCreationChatRequest() throws IOException {

        final String registrationUrl = "http://localhost:8080/registration";

        final String username = "Valery";
        final String password = "justdoit";

        final List<NameValuePair> registrationParameters = new ArrayList<>();
        registrationParameters.add(new BasicNameValuePair("username", username));
        registrationParameters.add(new BasicNameValuePair("password", password));
        registrationParameters.add(new BasicNameValuePair("confirmPassword", password));

        sendRequest(registrationUrl, registrationParameters);

        final String loginUrl = "http://localhost:8080/login";

        final List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("username", username));
        loginParameters.add(new BasicNameValuePair("password", password));

        HttpResponse loginPostResponse = sendRequest(loginUrl, loginParameters);
        JSONObject loggedInUserData = getResponseContent(loginPostResponse);

        final String createChatUrl = "http://localhost:8080/createchat";
        final String token = loggedInUserData.optString("token");
        final String userId = loggedInUserData.optString("userId");
        final String chatName = "just do it";

        final List<NameValuePair> chatCreationParameters = new ArrayList<>();
        chatCreationParameters.add(new BasicNameValuePair("token", token));
        chatCreationParameters.add(new BasicNameValuePair("userId", userId));
        chatCreationParameters.add(new BasicNameValuePair("chatName", chatName));

        HttpResponse chatCreationResponse = sendRequest(createChatUrl, chatCreationParameters);

        JSONObject chatData = getResponseContent(chatCreationResponse);

        System.out.println(chatData.optString("message"));

        final int expectedStatus = 200;

        assertEquals("Unexpected response status", expectedStatus, chatCreationResponse.getStatusLine().getStatusCode());
        assertEquals("Chat creation post failed", chatName, chatData.optString("chatName"));

    }

 
}
