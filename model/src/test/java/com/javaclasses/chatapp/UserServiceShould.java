package com.javaclasses.chatapp;


import com.javaclasses.chatapp.dto.LoginDto;
import com.javaclasses.chatapp.dto.RegistrationDto;
import com.javaclasses.chatapp.dto.TokenDto;
import com.javaclasses.chatapp.dto.UserDto;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.javaclasses.chatapp.ErrorType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();

    @Test
    public void registerUser() {

        final String username = "Alice";
        final String password = "fromwonderland";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final UserDto newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username, newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithDuplicateUsername() {

        final String username = "Scout";
        final String password = "freeparrots";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(DUPLICATE_USERNAME, e.getErrorType());
        }

    }

    @Test
    public void failRegisterUserWithNotMatchingPasswords() {

        final String username = "Jacob";
        final String password = "watertoelephants";
        final String confirmPassword = password + "123";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, confirmPassword);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(PASSWORDS_DO_NOT_MATCH, e.getErrorType());
        }

    }

    @Test
    public void trimUsernameUponRegistration() {

        final String username = " Jem ";
        final String password = "somethinghappend";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        UserId newUserId = null;
        try {
            newUserId = userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final UserDto newUser = userService.findRegisteredUserById(newUserId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

    }

    @Test
    public void failRegisterUserWithEmptyUsername() {

        final String username = "";
        final String password = "here's looking at you kid";

        final  RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES, e.getErrorType());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSapcesUsername() {

        final String username = "Doctor Zhivago";
        final String password = "one coffee please";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES, e.getErrorType());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword() {

        final String username = "Kevin";
        final String password = "";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(PASSWORD_IS_EMPTY, e.getErrorType());
        }

    }

    @Test
    public void loginUser() {

        final String username = "Mila";
        final String password = "lostinnewyork";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final LoginDto loginDto = new LoginDto(username, password);
        TokenDto token = null;
        try {
            token = userService.login(loginDto);
        } catch (AuthenticationException e) {
            fail("Registered user was not logged in");
        }

        final UserDto loggedInUser = userService.findLoggedInUserByToken(token);

        assertEquals("User was not logged in", username, loggedInUser.getUsername());

    }

    @Test
    public void failLoginUnregisteredUser() {

        final String username = "Jacob";
        final String password = "watertoelephants";

        final LoginDto loginDto = new LoginDto(username, password);

        try {
            userService.login(loginDto);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals(AUTHENTICATION_FAILED, e.getErrorType());
        }

    }

    @Test
    public void failLoginUserWithWrongPassword() {

        final String username = "Ilsa";
        final String password = "here's looking at you kid";

        final RegistrationDto registrationDto = new RegistrationDto(username, password, password);

        try {
            userService.register(registrationDto);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final String wrongPassword = password + "123";
        final LoginDto loginDto = new LoginDto(username, wrongPassword);

        try {
            userService.login(loginDto);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals(AUTHENTICATION_FAILED, e.getErrorType());
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
            tokens.add(future.get().getToken().getId());
        }

        if (userIds.size() != count) {
            fail("Generated user ids are not unique");
        }

        if (tokens.size() != count) {
            fail("Generated tokens are not unique");
        }

    }

}
