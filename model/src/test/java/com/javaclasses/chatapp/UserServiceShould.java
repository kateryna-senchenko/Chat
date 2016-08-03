package com.javaclasses.chatapp;


import com.javaclasses.chatapp.dto.LoginParametersDto;
import com.javaclasses.chatapp.dto.RegistrationParametersDto;
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

    private final String username = "Alice";
    private final String password = "fromwonderland";


    private UserId registerUser(String username, String password, String confirmPassword) throws RegistrationException {

        final RegistrationParametersDto registrationParametersDto = new RegistrationParametersDto(username, password, confirmPassword);
        return userService.register(registrationParametersDto);
    }

    private TokenDto loginUser(String username, String password) throws AuthenticationException {

        final LoginParametersDto loginDto = new LoginParametersDto(username, password);
        return userService.login(loginDto);
    }

    private void deleteRegisteredUser(UserId userId) {
        try {
            userService.deleteUser(userId);
        } catch (UserRemovalException e) {
            fail("Cannot delete user");
        }
    }

    private void logoutUser(TokenDto tokenDto){
        userService.logout(tokenDto);
    }

    @Test
    public void successfullyRegisterUser() {

        UserId userId = null;
        try {
            userId = registerUser(username, password, password);
        } catch (RegistrationException e) {
            fail("Failed to register new user");
        }

        final UserDto newUser = userService.findRegisteredUserById(userId);

        assertEquals("New user was not registered", username, newUser.getUsername());

        deleteRegisteredUser(userId);
    }

    @Test
    public void failRegisterUserWithDuplicateUsername() {

        UserId userId = null;
        try {
            userId = registerUser(username, password, password);
        } catch (RegistrationException e) {
            fail("Failed to register new user");
        }

        try {
            registerUser(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(DUPLICATE_USERNAME, e.getErrorType());
        }

        deleteRegisteredUser(userId);

    }

    @Test
    public void failRegisterUserWithNotMatchingPasswords() {

        final String confirmPassword = password + "123";

        final RegistrationParametersDto registrationParametersDto = new RegistrationParametersDto(username, password, confirmPassword);

        try {
            userService.register(registrationParametersDto);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(PASSWORDS_DO_NOT_MATCH, e.getErrorType());
        }


    }

    @Test
    public void trimUsernameUponRegistration() {

        UserId userId = null;
        try {
            userId = registerUser("  " + username + " ", password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final UserDto newUser = userService.findRegisteredUserById(userId);

        assertEquals("New user was not registered", username.trim(), newUser.getUsername());

        deleteRegisteredUser(userId);

    }

    @Test
    public void failRegisterUserWithEmptyUsername() {

        try {
            registerUser("", password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES, e.getErrorType());
        }

    }

    @Test
    public void failRegisterUserWithWhiteSpacesUsername() {

        final String username = "Doctor Zhivago";

        try {
            registerUser(username, password, password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(USERNAME_IS_EMPTY_OR_CONTAINS_WHITE_SPACES, e.getErrorType());
        }

    }

    @Test
    public void failRegisterUserWithEmptyPassword() {

        try {
            registerUser(username, "", password);
            fail("Expected RegistrationException was not thrown");
        } catch (RegistrationException e) {
            assertEquals(PASSWORD_IS_EMPTY, e.getErrorType());
        }

    }

    @Test
    public void successfullyLoginUser() {

        UserId userId = null;
        try {
            userId = registerUser(username, password, password);
        } catch (RegistrationException e) {
            fail("Failed to register new user");
        }

        TokenDto token = null;
        try {
            token = loginUser(username, password);
        } catch (AuthenticationException e) {
            fail("Registered user was not logged in");
        }

        final UserDto loggedInUser = userService.findLoggedInUserByToken(token);

        assertEquals("User was not logged in", username, loggedInUser.getUsername());

        logoutUser(token);
        deleteRegisteredUser(userId);
    }

    @Test
    public void failLoginUnregisteredUser() {

        try {
            loginUser(username, password);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals(AUTHENTICATION_FAILED, e.getErrorType());
        }

    }

    @Test
    public void failLoginUserWithWrongPassword() {

        UserId userId = null;
        try {
            userId = registerUser(username, password, password);
        } catch (RegistrationException e) {
            fail("New user was not registered");
        }

        final String wrongPassword = password + "123";

        try {
            loginUser(username, wrongPassword);
            fail("Expected AuthenticationException was not thrown");
        } catch (AuthenticationException e) {
            assertEquals(AUTHENTICATION_FAILED, e.getErrorType());
        }

        deleteRegisteredUser(userId);
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


            final UserId userId = registerUser(username, password, password);

            final TokenDto tokenDto = loginUser(username, password);

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
            tokens.add(future.get().getTokenId().getId());

            logoutUser((future.get()));
            deleteRegisteredUser(future.get().getUserId());
        }

        if (userIds.size() != count) {
            fail("Generated user ids are not unique");
        }

        if (tokens.size() != count) {
            fail("Generated tokens are not unique");
        }

    }

}
