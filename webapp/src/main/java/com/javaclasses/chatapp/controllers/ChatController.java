package com.javaclasses.chatapp.controllers;

import com.javaclasses.chatapp.*;
import com.javaclasses.chatapp.dto.*;
import com.javaclasses.chatapp.impl.ChatServiceImpl;
import com.javaclasses.chatapp.impl.UserServiceImpl;
import com.javaclasses.chatapp.tinytypes.ChatId;
import com.javaclasses.chatapp.tinytypes.TokenId;
import com.javaclasses.chatapp.tinytypes.UserId;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.javaclasses.chatapp.Parameters.*;


public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    private static ChatController chatController = new ChatController();

    private final ChatService chatService = ChatServiceImpl.getInstance();
    private final UserService userService = UserServiceImpl.getInstance();
    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private ChatController() {
        createChat();
        addMemberToChat();
        removeMemberFromChat();
        postMassage();
        deleteChat();
    }

    public static ChatController getInstance() {
        return chatController;
    }

    private void createChat() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/create-chat", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final String userId = request.getParameter(USER_ID);
            final String chatName = request.getParameter(CHAT_NAME);

            final UserId id = new UserId(Long.valueOf(userId));
            final TokenEntityDto tokenEntityDto = new TokenEntityDto(new TokenId(UUID.fromString(tokenId)), id);
            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenEntityDto) == null) {

                if (log.isInfoEnabled()) {
                    log.info("Forbidden chat creation operation");
                }

                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");
            }

            final ChatCreationParametersDto chatCreationParametersDto = new ChatCreationParametersDto(id, chatName);

            try {
                final ChatId chatId = chatService.createChat(chatCreationParametersDto);

                final Collection<ChatEntityDto> allChats = chatService.findAllChats();
                List<String> chatNames = new ArrayList<>();
                for (ChatEntityDto chat : allChats) {
                    chatNames.add(chat.getChatName());
                }
                final JSONArray chatList = new JSONArray(chatNames);

                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(CHAT_NAME, chatService.findChatById(chatId).getChatName());
                handlerProcessingResult.setData(TOKEN_ID, tokenId);
                handlerProcessingResult.setData(CHAT_LIST, chatList.toString());

                if (log.isInfoEnabled()) {
                    log.info("Created chat {}", chatName);
                }

            } catch (ChatCreationException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if (log.isInfoEnabled()) {
                    log.info("Failed to create chat {}", chatName);
                }
            }

            return handlerProcessingResult;
        });
    }

    private void addMemberToChat() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/join-chat", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final String chatName = request.getParameter(CHAT_NAME);

            final TokenEntityDto tokenEntityDto = new TokenEntityDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenEntityDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if (log.isInfoEnabled()) {
                    log.info("Forbidden joining chat operation");
                }

            }

            final ChatEntityDto chatEntityDto = chatService.findChatByName(chatName);
            final ChatId chatId = chatEntityDto.getChatId();
            final MemberChatParametersDto memberChatParametersDto = new MemberChatParametersDto(userId, chatId);

            try {
                chatService.addMember(memberChatParametersDto);

                final List<MessageEntityDto> messages = chatEntityDto.getMessages();

                final List<String> results = new ArrayList<>();

                for (MessageEntityDto messageEntityDto : messages) {
                    results.add(messageEntityDto.getAuthorName() + ": " + messageEntityDto.getMessage());
                }
                final JSONArray chatMessages = new JSONArray(results);

                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(TOKEN_ID, tokenId);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(USERNAME, userService.findRegisteredUserById(userId).getUsername());
                handlerProcessingResult.setData(CHAT_NAME, chatService.findChatById(chatId).getChatName());
                handlerProcessingResult.setData(MESSAGE_LIST, chatMessages.toString());

                if (log.isInfoEnabled()) {
                    log.info("User {} has joined chat {}", userId.getId(), chatId.getId());
                }

            } catch (MembershipException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if (log.isInfoEnabled()) {
                    log.info("User {} failed to join chat {}", userId.getId(), chatId.getId());
                }

            }
            return handlerProcessingResult;
        });
    }

    private void removeMemberFromChat() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/leave-chat", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));

            final TokenEntityDto tokenEntityDto = new TokenEntityDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenEntityDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if (log.isInfoEnabled()) {
                    log.info("Forbidden leaving chat operation");
                }

            }
            final MemberChatParametersDto memberChatParametersDto = new MemberChatParametersDto(userId, chatId);

            try {
                chatService.removeMember(memberChatParametersDto);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(TOKEN_ID, tokenId);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(USERNAME, userService.findRegisteredUserById(userId).getUsername());
                handlerProcessingResult.setData(CHAT_NAME, chatService.findChatById(chatId).getChatName());

                if (log.isInfoEnabled()) {
                    log.info("User {} has left chat {}", userId.getId(), chatId.getId());
                }

            } catch (MembershipException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if (log.isInfoEnabled()) {
                    log.info("User {} failed to leave chat {}", userId.getId(), chatId.getId());
                }

            }
            return handlerProcessingResult;
        });
    }

    private void postMassage() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/post-message", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));
            final String message = request.getParameter(CHAT_MESSAGE);

            final TokenEntityDto tokenEntityDto = new TokenEntityDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenEntityDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if (log.isInfoEnabled()) {
                    log.info("Forbidden posting message operation");
                }

            }

            final PostMessageParametersDto postMessageParametersDto =
                    new PostMessageParametersDto(userId, userService.findRegisteredUserById(userId).getUsername(), chatId, message);

            try {
                chatService.postMessage(postMessageParametersDto);

                final ChatEntityDto chatEntityDto = chatService.findChatById(chatId);
                final List<MessageEntityDto> messages = chatEntityDto.getMessages();

                final List<String> results = new ArrayList<>();

                for (MessageEntityDto messageEntityDto : messages) {
                    results.add(messageEntityDto.getAuthorName() + ": " + messageEntityDto.getMessage());
                }
                final JSONArray chatMessages = new JSONArray(results);
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
                handlerProcessingResult.setData(TOKEN_ID, tokenId);
                handlerProcessingResult.setData(USER_ID, String.valueOf(userId.getId()));
                handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));
                handlerProcessingResult.setData(CHAT_MESSAGE, message);
                handlerProcessingResult.setData(MESSAGE_LIST, chatMessages.toString());

                if (log.isInfoEnabled()) {
                    log.info("User {} has posted a message to chat {}", userId.getId(), chatId.getId());
                }

            } catch (PostMessageException e) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                handlerProcessingResult.setData(ERROR_MESSAGE, e.getMessage());

                if (log.isInfoEnabled()) {
                    log.info("User {} failed to post a message to chat {}", userId.getId(), chatId.getId());
                }
            }

            return handlerProcessingResult;
        });
    }

    private void deleteChat() {

        handlerRegistry.registerHandler(new CompoundKey("/chat/delete-chat", "POST"), request -> {

            final String tokenId = request.getParameter(TOKEN_ID);
            final UserId userId = new UserId(Long.valueOf(request.getParameter(USER_ID)));
            final ChatId chatId = new ChatId(Long.valueOf(request.getParameter(CHAT_ID)));

            final TokenEntityDto tokenEntityDto = new TokenEntityDto(new TokenId(UUID.fromString(tokenId)), userId);

            HandlerProcessingResult handlerProcessingResult;
            if (userService.findLoggedInUserByToken(tokenEntityDto) == null) {
                handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_FORBIDDEN);
                handlerProcessingResult.setData(ERROR_MESSAGE, "Cannot find user");

                if (log.isInfoEnabled()) {
                    log.info("Forbidden chat deletion operation");
                }

            }

            chatService.deleteChat(chatId);
            handlerProcessingResult = new HandlerProcessingResult(HttpServletResponse.SC_OK);
            handlerProcessingResult.setData(TOKEN_ID, tokenId);
            handlerProcessingResult.setData(CHAT_ID, String.valueOf(chatId.getId()));

            if (log.isInfoEnabled()) {
                log.info("Chat {} has been deleted", chatId.getId());
            }

            return handlerProcessingResult;

        });
    }

}
