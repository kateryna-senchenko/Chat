package com.javaclasses.chatapp;


import com.javaclasses.chatapp.dto.LoginDTO;
import com.javaclasses.chatapp.dto.RegistrationDTO;
import com.javaclasses.chatapp.dto.UserDTO;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    @Test
    public void registerUser() {

        String username = "Alice";
        String password = "fromwonderland";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDTO);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDTO newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithDuplicateUsername() {

        String username = "Scout";
        String password = "freeparrots";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        try {
            userService.register(registrationDTO);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Specified username is not available", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithNotMatchingPasswords() {

        String username = "Jacob";
        String password = "watertoelephants";
        String confirmPassword = password + "123";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, confirmPassword);

        try {
            userService.register(registrationDTO);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Passwords do not match", e.getMessage());
        }

    }

    @Test
    public void trimUsername() {

        String username = " Jem ";
        String password = "somethinghappend";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDTO);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDTO newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithEmptyUsername() {

        String username = "";
        String password = "here's looking at you kid";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSapcesUsername() {

        String username = "Doctor Zhivago";
        String password = "one coffee please";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword() {

        String username = "Kevin";
        String password = "";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Password should not be empty", e.getMessage());
        }

    }

    @Test
    public void loginUser() {

        String username = "Mila";
        String password = "lostinnewyork";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        LoginDTO loginDTO = new LoginDTO(username, password);
        Token token = null;
        try {
            token = userService.login(loginDTO);
        } catch (AuthenticationException e) {
            fail("Registered user was not logged in");
        }

        UserDTO loggedInUser = userService.findLoggedInUserByToken(token);

        assertEquals("User was not logged in", username, loggedInUser.getUsername());

    }

    @Test
    public void failLoginUnregisteredUser() {

        String username = "Jacob";
        String password = "watertoelephants";

        LoginDTO loginDTO = new LoginDTO(username, password);

        try {
            userService.login(loginDTO);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

    @Test
    public void failLoginUserWithWrongPassword() {

        String username = "Ilsa";
        String password = "here's looking at you kid";

        RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);

        try {
            userService.register(registrationDTO);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        String wrongPassword = password + "123";
        LoginDTO loginDTO = new LoginDTO(username, wrongPassword);

        try {
            userService.login(loginDTO);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

    @Test
    public void beSafeInMultithreading() throws ExecutionException, InterruptedException {

        final int count = 100;
        final ExecutorService executor = Executors.newFixedThreadPool(count);
        final CountDownLatch startLatch = new CountDownLatch(count);
        final List<Future<Token>> results = new ArrayList<>();
        AtomicInteger someDifferenceInUsername = new AtomicInteger(0);

        Callable<Token> callable = new Callable<Token>() {

            @Override
            public Token call() throws Exception {

                startLatch.countDown();
                startLatch.await();

                String username = "username" + someDifferenceInUsername.getAndIncrement();
                String password = "password";

                RegistrationDTO registrationDTO = new RegistrationDTO(username, password, password);
                userService.register(registrationDTO);

                LoginDTO loginDTO = new LoginDTO(username, password);

                return userService.login(loginDTO);
            }
        };

        for (int i = 0; i < count; i++) {

            Future<Token> future = executor.submit(callable);
            results.add(future);
        }

        Set<Long> userIds = new HashSet<>();
        Set<UUID> tokens = new HashSet<>();

        for (Future<Token> future : results) {
            userIds.add(future.get().getUserId().getId());
            tokens.add(future.get().getToken());
        }

        if (userIds.size() != count) {
            fail("Generated user ids are not unique");
        }

        if (tokens.size() != count) {
            fail("Generated tokens are not unique");
        }

    }

}
