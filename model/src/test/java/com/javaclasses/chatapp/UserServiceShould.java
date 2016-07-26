package com.javaclasses.chatapp;


import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.storage.Repository;
import org.junit.Before;
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

        User newUser = (User)userService.getRepository().getItem(newUserId);

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

        User newUser = (User)userService.getRepository().getItem(newUserId);

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

}
