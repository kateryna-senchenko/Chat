package com.javaclasses.chatapp;

/**
 * Public API of UserService
 */
public interface UserService {

    /**
     * Registers new user
     * @param username - new username
     * @param password - user password
     * @param confirmPassword - user confirmation password
     * @return unique UserId
     * @throws RegistrationException if registration fails
     */
    UserId register(String username, String password, String confirmPassword) throws RegistrationException;

    /**
     * Logins registered user
     * @param username - user name
     * @param password - user password
     * @return access Token
     * @throws AuthenticationException if authentication fails
     */
    Token login(String username, String password) throws AuthenticationException;

    /**
     * Gets registered user by user id
     * @param id - user id
     * @return user instance
     */
    User findRegisteredUserById(UserId id);

    /**
     * Gets logged in user by token
     * @param token - access token
     * @return user instance
     */
    User findLoggedInUserByToken(Token token);
}
