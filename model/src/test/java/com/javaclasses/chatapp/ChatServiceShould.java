package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.*;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.javaclasses.chatapp.ErrorType.*;
import static org.junit.Assert.*;

public class ChatServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private final String username = "Scout";
    private final String chatName = "Protect the Mockingbirds";
    private final String message = "Hello there!";

    private UserId userId;
    private TokenDto token;

    @Before
    public void registerAndLoginUser() {

        String password = "Finch";
        final RegistrationParametersDto registrationParametersDto = new RegistrationParametersDto(username, password, password);

        try {
            userId = userService.register(registrationParametersDto);
        } catch (RegistrationException e) {
            fail("Failed to register new user");
        }

        final LoginParametersDto loginDto = new LoginParametersDto(username, password);

        try {
            token = userService.login(loginDto);
        } catch (AuthenticationException e) {
            fail("Failed to login user");
        }

    }

    @After
    public void logoutAndDeleteUser() {
        userService.logout(token);
        try {
            userService.deleteUser(userId);
        } catch (UserRemovalException e) {
            fail("Cannot delete user");
        }
    }


    private ChatId createChat(UserId userId, String chatName) throws ChatCreationException {
        final ChatCreationParametersDto chatCreationParametersDto = new ChatCreationParametersDto(userId, chatName);
        return chatService.createChat(chatCreationParametersDto);

    }

    private void deleteChat(ChatId chatId) {
        chatService.deleteChat(chatId);
    }

    private void addMemberToChat(UserId userId, ChatId chatId) throws MembershipException {
        final MemberChatParametersDto memberChatParametersDto = new MemberChatParametersDto(userId, chatId);
        chatService.addMember(memberChatParametersDto);
    }


    private void removeMemberFromChat(UserId userId, ChatId chatId) throws MembershipException {
        final MemberChatParametersDto memberChatParametersDto = new MemberChatParametersDto(userId, chatId);
        chatService.removeMember(memberChatParametersDto);
    }

    private void postMessage(UserId userId, String username, ChatId chatId, String message) throws PostMessageException {
        final PostMessageParametersDto postMessageParametersDto = new PostMessageParametersDto(userId, username, chatId, message);
        chatService.postMessage(postMessageParametersDto);
    }

    @Test
    public void successfullyCreateChat() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }
        assertEquals("New chat was not created", chatName, chatService.findChatById(chatId).getChatName());
        assertEquals("Wrong chat owner", userId, chatService.findChatById(chatId).getOwner());

        deleteChat(chatId);
    }

    @Test
    public void failToCreateChatWithDuplicateName() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("New chat was not created");
        }

        try {
            createChat(userId, chatName);
            fail("Expected ChatCreationException was not thrown");
        } catch (ChatCreationException e) {
            assertEquals(DUPLICATE_CHATNAME, e.getErrorType());
        }

        deleteChat(chatId);

    }

    @Test
    public void trimChatNameUponCreation() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, "  " + chatName + " ");
        } catch (ChatCreationException e) {
            fail("New chat was not created");
        }

        assertEquals("New chat was not created", chatName.trim(), chatService.findChatById(chatId).getChatName());
        assertEquals("Wrong chat owner", userId, chatService.findChatById(chatId).getOwner());

        deleteChat(chatId);

    }

    @Test
    public void failToCreateChatIfNameIsEmpty() {

        try {
            createChat(userId, "");
            fail("Expected ChatCreationException was not thrown");
        } catch (ChatCreationException e) {
            assertEquals(CHATNAME_IS_EMPTY, e.getErrorType());
        }

    }

    @Test
    public void successfullyAddMemberToChat() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }

        try {
            addMemberToChat(userId, chatId);
        } catch (MembershipException e) {
            fail("Failed to add member to chat");
        }
        final List<UserId> members = chatService.findChatById(chatId).getMembers();

        assertTrue("Member failed to join the chat", members.contains(userId));

        deleteChat(chatId);
    }

    @Test
    public void failToAddMemberIfAlreadyThere() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }

        try {
            addMemberToChat(userId, chatId);
        } catch (MembershipException e) {
            fail("Failed to add member to chat");
        }
        try {
            addMemberToChat(userId, chatId);
            fail("Expected MembershipException was not thrown");
        } catch (MembershipException e) {
            assertEquals(ALREADY_A_MEMBER, e.getErrorType());
        }

        deleteChat(chatId);
    }

    @Test
    public void successfullyRemoveMemberFromChat() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }
        try {
            addMemberToChat(userId, chatId);
        } catch (MembershipException e) {
            fail("Failed to add member to chat");
        }
        try {
            removeMemberFromChat(userId, chatId);
        } catch (MembershipException e) {
            fail("Failed to remove member from chat");
        }
        List<UserId> members = chatService.findChatById(chatId).getMembers();

        assertFalse("Member was not removed from the chat", members.contains(userId));

        deleteChat(chatId);
    }

    @Test
    public void failToLeaveChatIfNotAMember() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }
        try {
            removeMemberFromChat(userId, chatId);
            fail("Expected MembershipException was not thrown");
        } catch (MembershipException e) {
            assertEquals(IS_NOT_A_MEMBER, e.getErrorType());
        }

        deleteChat(chatId);

    }

    @Test
    public void successfullyPostMessage() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }
        try {
            addMemberToChat(userId, chatId);
        } catch (MembershipException e) {
            fail("Failed to add member to chat");
        }
        try {
            postMessage(userId, username, chatId, message);
        } catch (PostMessageException e) {
            fail("Message was not posted");
        }

        final MessageDto messageData = new MessageDto(username, chatId, message);
        final List<MessageDto> messages = chatService.findChatById(chatId).getMessages();

        assertTrue("Message was not posted", messages.contains(messageData));

        deleteChat(chatId);
    }

    @Test
    public void failToPostMessageIfNotAMember() {

        ChatId chatId = null;
        try {
            chatId = createChat(userId, chatName);
        } catch (ChatCreationException e) {
            fail("Failed to create new chat");
        }
        try {
            postMessage(userId, username, chatId, message);
            fail("Expected PostMessageException was not thrown");
        } catch (PostMessageException e) {
            assertEquals(IS_NOT_A_MEMBER, e.getErrorType());
        }

        deleteChat(chatId);

    }
}
