package com.javaclasses.chatapp;


import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.dto.UserDto;
import com.javaclasses.chatapp.entities.Token;
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

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDto newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithDuplicateUsername() {

        String username = "Scout";
        String password = "freeparrots";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        try {
            userService.register(registrationDto);
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

        RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Passwords do not match", e.getMessage());
        }

    }

    @Test
    public void trimUsername() {

        String username = " Jem ";
        String password = "somethinghappend";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        UserDto newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithEmptyUsername() {

        String username = "";
        String password = "here's looking at you kid";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSapcesUsername() {

        String username = "Doctor Zhivago";
        String password = "one coffee please";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Username should not be empty or contain white spaces", e.getMessage());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword() {

        String username = "Kevin";
        String password = "";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals("Password should not be empty", e.getMessage());
        }

    }

    @Test
    public void loginUser() {

        String username = "Mila";
        String password = "lostinnewyork";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        LoginDto loginDto = new LoginDto(username, password);
        TokenDto token = null;
        try {
            token = userService.login(loginDto);
        } catch (AuthenticationException e) {
            fail("Registered user was not logged in");
        }

        UserDto loggedInUser = userService.findLoggedInUserByToken(token);

        assertEquals("User was not logged in", username, loggedInUser.getUsername());

    }

    @Test
    public void failLoginUnregisteredUser() {

        String username = "Jacob";
        String password = "watertoelephants";

        LoginDto loginDto = new LoginDto(username, password);

        try {
            userService.login(loginDto);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals("Specified combination of username and password was not found", e.getMessage());
        }

    }

    @Test
    public void failLoginUserWithWrongPassword() {

        String username = "Ilsa";
        String password = "here's looking at you kid";

        RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        String wrongPassword = password + "123";
        LoginDto loginDto = new LoginDto(username, wrongPassword);

        try {
            userService.login(loginDto);
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
        final List<Future<TokenDto>> results = new ArrayList<>();
        AtomicInteger someDifferenceInUsername = new AtomicInteger(0);

        Callable<TokenDto> callable = () -> {

            startLatch.countDown();
            startLatch.await();

            final String username = "username" + someDifferenceInUsername.getAndIncrement();
            final String password = "password";

            final RegistrationDto registrationDto = new RegistrationDto(username, password, password);
            final UserId userId = userService.register(registrationDto);

            final LoginDto loginDto = new LoginDto(username, password);

            final TokenDto tokenDto = userService.login(loginDto);

            assertEquals("User ids after registration and login are not the same", userId, tokenDto.getUserId());

            return tokenDto;
        };

        for (int i = 0; i < count; i++) {

            Future<TokenDto> future = executor.submit(callable);
            results.add(future);
        }

        final Set<Long> userIds = new HashSet<>();
        final Set<UUID> tokens = new HashSet<>();

        for (Future<TokenDto> future : results) {
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
