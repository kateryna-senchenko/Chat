package com.javaclasses.chatapp;


import com.javaclasses.chatapp.impl.UserServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserServiceImpl userService = UserServiceImpl.getInstance();

    @Test
    public void registerUser() throws RegistrationException {

        String username = "Alice";
        String password = "fromwonderland";
        String confirmPassword = password;

        UserId newUserId = userService.register(username, password, confirmPassword);

        User newUser = (User)userService.getUserRepository().getItem(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithDuplicateUsername() throws RegistrationException {

        String username = "Scout";
        String password = "freeparrots";
        String confirmPassword = password;

        userService.register(username, password, confirmPassword);

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Specified username is not available", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithNotMatchingPasswords(){

        String username = "Jacob";
        String password = "watertoelephants";
        String confirmPassword = "watertoelephant";

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Passwords do not match", e.getMessage());
        }

    }

    @Test
    public void trimUsername() throws RegistrationException {

        String username = " Jem ";
        String password = "somethinghappend";
        String confirmPassword = password;

        UserId newUserId = userService.register(username, password, confirmPassword);

        User newUser = (User)userService.getUserRepository().getItem(newUserId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithEmptyUsername(){

        String username = "";
        String password = "here's looking at you kid";
        String confirmPassword = password;

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSapcesUsername(){

        String username = "Doctor Zhivago";
        String password = "one coffee please";
        String confirmPassword = password;

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword(){

        String username = "Kevin";
        String password = "";
        String confirmPassword = password;

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Password should not be empty", e.getMessage());
        }

    }

    @Test
    public void loginUser() throws RegistrationException, AuthenticationException {

        String username = "Mila";
        String password = "lostinnewyork";
        String confirmPassword = password;

        userService.register(username, password, confirmPassword);

        Token token = userService.login(username, password);

        User loggedInUser = (User)userService.getLoggedInUserRepository().getItem(token);

        assertEquals("User was not logged in", username, loggedInUser.getUsername());

    }

    @Test
    public void failLoginUnregisteredUser(){

        String username = "Jacob";
        String password = "watertoelephants";

        try {
            userService.login(username, password);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

    @Test
    public void failLoginUserWithWrongPassword() throws RegistrationException{

        String username = "Ilsa";
        String password = "here's looking at you kid";
        String confirmPassword = password;

        userService.register(username, password, confirmPassword);

        String wrongPassword = "here's looking at you, kid";

        try {
            userService.login(username, wrongPassword);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

}
