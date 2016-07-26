package com.javaclasses.chatapp;


import com.javaclasses.chatapp.dto.UserDTO;
import com.javaclasses.chatapp.entities.User;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    @Test
    public void registerUser() {

        String username = "Alice";
        String password = "fromwonderland";

        UserId newUserId = null;
        try {
            newUserId = userService.register(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDTO newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithDuplicateUsername()  {

        String username = "Scout";
        String password = "freeparrots";


        try {
            userService.register(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        try {
            userService.register(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Specified username is not available", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithNotMatchingPasswords(){

        String username = "Jacob";
        String password = "watertoelephants";
        String confirmPassword = password + "123";

        try {
            userService.register(username, password, confirmPassword);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Passwords do not match", e.getMessage());
        }

    }

    @Test
    public void trimUsername() {

        String username = " Jem ";
        String password = "somethinghappend";

        UserId newUserId = null;
        try {
            newUserId = userService.register(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDTO newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithEmptyUsername(){

        String username = "";
        String password = "here's looking at you kid";

        try {
            userService.register(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSapcesUsername(){

        String username = "Doctor Zhivago";
        String password = "one coffee please";

        try {
            userService.register(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword(){

        String username = "Kevin";
        String password = "";

        try {
            userService.register(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Password should not be empty", e.getMessage());
        }

    }

    @Test
    public void loginUser()  {

        String username = "Mila";
        String password = "lostinnewyork";

        try {
            userService.register(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        Token token = null;
        try {
            token = userService.login(username, password);
        } catch (AuthenticationException e) {
            fail("Registered user was not logged in");
        }

        UserDTO loggedInUser = userService.findLoggedInUserByToken(token);

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
    public void failLoginUserWithWrongPassword() {

        String username = "Ilsa";
        String password = "here's looking at you kid";

        try {
            userService.register(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        String wrongPassword = password + "123";

        try {
            userService.login(username, wrongPassword);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

}
