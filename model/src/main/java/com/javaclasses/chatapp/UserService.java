package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.LoginParametersDto;
import com.javaclasses.chatapp.dto.RegistrationParametersDto;
import com.javaclasses.chatapp.dto.TokenEntityDto;
import com.javaclasses.chatapp.dto.UserEntityDto;
import com.javaclasses.chatapp.tinytypes.UserId;


/**
 * Public API of UserService
 */
public interface UserService {

    /**
     * Registers new user
     * @param registrationParametersDto - contains String username, String password, String confirmPassword
     * @return unique UserId
     * @throws RegistrationException if registration fails
     */
    UserId register(RegistrationParametersDto registrationParametersDto) throws RegistrationException;

    /**
     * Logins registered user
     * @param loginDto - contains String username and String password
     * @return access Token DTO
     * @throws AuthenticationException if authentication fails
     */
    TokenEntityDto login(LoginParametersDto loginDto) throws AuthenticationException;

    /**
     * Provides access to registered user dto by user id
     * @param id - user id
     * @return user dto instance
     */
    UserEntityDto findRegisteredUserById(UserId id);

    /**
     * Provides access to logged in user dto by token
     * @param token - access token DTO
     * @return user dto instance
     */
    UserEntityDto findLoggedInUserByToken(TokenEntityDto token);

    /**
     * Removes registered user
     * @param id - id of the user to be removed
     */
    void deleteUser(UserId id) throws UserRemovalException;

    /**
     * Logs out logged in user
     * @param token - access token
     */
    void logout(TokenEntityDto token);
}
