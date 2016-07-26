package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.LoginDTO;
import com.javaclasses.chatapp.dto.RegistrationDTO;
import com.javaclasses.chatapp.dto.UserDTO;


/**
 * Public API of UserService
 */
public interface UserService {

    /**
     * Registers new user
     * @param registrationDTO - contains String username, String password, String confirmPassword
     * @return unique UserId
     * @throws RegistrationException if registration fails
     */
    UserId register(RegistrationDTO registrationDTO) throws RegistrationException;

    /**
     * Logins registered user
     * @param loginDTO - contains String username and String password
     * @return access Token
     * @throws AuthenticationException if authentication fails
     */
    Token login(LoginDTO loginDTO) throws AuthenticationException;

    /**
     * Gets registered user dto by user id
     * @param id - user id
     * @return user dto instance
     */
    UserDTO findRegisteredUserById(UserId id);

    /**
     * Gets logged in user dto by token
     * @param token - access token
     * @return user dto instance
     */
    UserDTO findLoggedInUserByToken(Token token);
}
