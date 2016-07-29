package com.javaclasses.chatapp;

import com.javaclasses.chatapp.dto.*;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ChatServiceShould {

    private final UserService userService = UserServiceImpl.getInstance();
    private final ChatService chatService = ChatServiceImpl.getInstance();

    private UserId firstUserId;
    private UserId secondUserId;
    private ChatId chatId;
    private final String chatName = "Protect the Mockingbirds";

    @Before
    public void registerAndLoginUsers() {

        userService.deleteAll();

        final String atticusUsername = "Atticus";
        final String atticusPassword = "Finch";

        final String selmaUsername = "Selma";
        final String selmaPassword = "BloodySundayAfternoon";

        final RegistrationDto atticusRegistrationDto =
                new RegistrationDto(atticusUsername, atticusPassword, atticusPassword);

        final RegistrationDto selmaRegistrationDto =
                new RegistrationDto(selmaUsername, selmaPassword, selmaPassword);

        firstUserId = null;
        secondUserId = null;
        try {
            firstUserId = userService.register(atticusRegistrationDto);
            secondUserId = userService.register(selmaRegistrationDto);
        } catch (RegistrationException e) {
            fail("New users were not registered");
        }

        final LoginDto atticusLoginDto = new LoginDto(atticusUsername, atticusPassword);
        final LoginDto selmaLoginDto = new LoginDto(selmaUsername, selmaPassword);

        try {
            userService.login(atticusLoginDto);
            userService.login(selmaLoginDto);
        } catch (AuthenticationException e) {
            fail("Registered users were not authenticated");
        }
    }

    @Before
    public void createChatAndAddMember(){

        chatService.deleteAll();

        final ChatCreationDto chatCreationDto = new ChatCreationDto(firstUserId, chatName);

        chatId = null;
        try {
            chatId = chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            fail("New chat was not created");
        }

        final MemberChatDto memberChatDto= new MemberChatDto(firstUserId, chatId);

        try {
            chatService.addMember(memberChatDto);
        } catch (MembershipException e) {
            fail("Member could not join the chat");
        }
    }

    @Test
    public void createChat(){

        assertEquals("New chat was not created", chatName, chatService.findChatById(chatId).getChatName());
        assertEquals("Wrong chat owner", firstUserId, chatService.findChatById(chatId).getOwner());

    }

    @Test
    public void failToCreateChatWithDuplicateName(){

        final String chatName = "Let's save Tom Robinson";

        final ChatCreationDto chatCreationDto = new ChatCreationDto(firstUserId, chatName);

        try {
            chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            fail("New chat was not created");
        }

        try {
            chatService.createChat(chatCreationDto);
            fail("Expected ChatCreationException was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Specified name is not available", e.getMessage());
        }

    }

    @Test
    public void trimChatName(){

        final String chatName = " Hush Scout!  ";

        final ChatCreationDto chatCreationDto = new ChatCreationDto(firstUserId, chatName);

        ChatId chatId = null;
        try {
            chatId = chatService.createChat(chatCreationDto);
        } catch (ChatCreationException e) {
            fail("New chat was not created");
        }

        assertEquals("New chat was not created", chatName.trim(), chatService.findChatById(chatId).getChatName());
        assertEquals("Wrong chat owner", firstUserId, chatService.findChatById(chatId).getOwner());

    }

    @Test
    public void failToCreateChatIfNameIsEmpty(){

        final String chatName = " ";

        final ChatCreationDto chatCreationDto = new ChatCreationDto(firstUserId, chatName);

        try {
            chatService.createChat(chatCreationDto);
            fail("Expected ChatCreationException was not thrown");
        } catch (ChatCreationException e) {
            assertEquals("Chat name should not be empty", e.getMessage());
        }

    }

    @Test
    public void addMemberToChat(){

        final List<UserId> members = chatService.findChatById(chatId).getMembers();

        assertTrue("Member failed to join the chat", members.contains(firstUserId));
    }

    @Test
    public void failToAddMemberIfAlreadyThere(){

        final MemberChatDto memberChatDto= new MemberChatDto(firstUserId, chatId);

        try {
            chatService.addMember(memberChatDto);
            fail("Expected MembershipException was not thrown");
        } catch (MembershipException e) {
            assertEquals("User " + firstUserId.getId() + " is already a member", e.getMessage());
        }
    }

    @Test
    public void removeMemberFromChat(){

        List<UserId> members = chatService.findChatById(chatId).getMembers();
        assertTrue("Member is not in the chat", members.contains(firstUserId));

        final MemberChatDto memberChatDto= new MemberChatDto(firstUserId, chatId);

        try {
            chatService.removeMember(memberChatDto);
        } catch (MembershipException e) {
            fail("Member failed to leave the chat");
        }

        members = chatService.findChatById(chatId).getMembers();

        assertFalse("Member was not removed from the chat", members.contains(firstUserId));
    }

    @Test
    public void failToLeaveChatIfNotAMember(){

        final List<UserId> members = chatService.findChatById(chatId).getMembers();

        assertFalse("Selma is a member", members.contains(secondUserId));

        final MemberChatDto memberChatDto= new MemberChatDto(secondUserId, chatId);

        try {
            chatService.removeMember(memberChatDto);
            fail("Expected MembershipException was not thrown");
        } catch (MembershipException e) {
            assertEquals("User " + secondUserId.getId() + " is not a member", e.getMessage());
        }

    }

    @Test
    public void postMessage(){

        final String message = "Hello there!";
        final PostMessageDto postMessageDto = new PostMessageDto(firstUserId, chatId, message);

        try {
            chatService.postMessage(postMessageDto);
        } catch (PostMessageException e) {
            fail("Message was not posted");
        }

        final Message messageData = new Message(firstUserId, chatId, message);

        final List<Message> messages = chatService.findChatById(chatId).getMessages();

        assertTrue("Message was not posted", messages.contains(messageData));
    }

    @Test
    public void failToPostMessageIfNotAMember(){

        final List<UserId> members = chatService.findChatById(chatId).getMembers();

        assertFalse("Selma is a member", members.contains(secondUserId));

        final String message = "Hello there!";
        final PostMessageDto postMessageDto = new PostMessageDto(secondUserId, chatId, message);

        try {
            chatService.postMessage(postMessageDto);
            fail("Expected PostMessageException was not thrown");
        } catch (PostMessageException e) {
           assertEquals("Cannot post message if not a member", e.getMessage());
        }


    }
}
