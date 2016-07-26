package com.javaclasses.chatapp;


import com.javaclasses.chatapp.impl.UserServiceImpl;
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
        String confirmPassword = "fromwonderland";

        UserId newUserId = userService.register(username, password, confirmPassword);

        User newUser = (User) userService.getRepository().getItem(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Before
    public void registerDuplicateUser() throws RegistrationException{

        String username = "Scout";
        String password = "protectthemockingbirds";
        String confirmPassword = "protectthemockingbirds";

        userService.register(username, password, confirmPassword);

    }

    @Test
    public void failRegisterUserWithDuplicateUsername(){

        String username = "Scout";
        String password = "freeparrots";
        String confirmPassword = "freeparrots";

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

}
