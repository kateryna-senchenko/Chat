var ChatService = function (eventbus, events, baseUrl) {


    var _createChat = function (chatData) {

        var tokenId = chatData.token;
        var userId = chatData.owner;
        var chatName = chatData.name;

        $.post(baseUrl + "chat/create-chat",
            {
                tokenId: tokenId,
                userId: userId,
                chatName: chatName
            },
            function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.CHAT_IS_CREATED, data.chatList);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.CHAT_CREATION_FAILED, data);
            })
    };

    var _addMember = function (chatData) {

        var tokenId = chatData.token;
        var userId = chatData.userId;
        var chatName = chatData.chatName;

        $.post(baseUrl + "chat/join-chat",
            {
                tokenId: tokenId,
                userId: userId,
                chatName: chatName
            },
            function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.MEMBER_IS_ADDED, data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.FAILED_TO_ADD_MEMBER, data);
            })
    };

    var _removeMember = function (chatData) {

        var tokenId = chatData.tokenId;
        var userId = chatData.userId;
        var chatId = chatData.chatId;

        $.post(baseUrl + "chat/leave-chat",
            {
                tokenId: tokenId,
                userId: userId,
                chatId: chatId
            },
            function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.MEMBER_HAS_LEFT, data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.FAILED_TO_LEAVE_CHAT, data);
            })

    };

    var _postMessage = function (chatData) {

        var tokenId = chatData.token;
        var userId = chatData.author;
        var chatId = chatData.chatId;
        var chatMessage = chatData.message;

        $.post(baseUrl + "chat/post-message",
            {
                tokenId: tokenId,
                userId: userId,
                chatId: chatId,
                chatMessage: chatMessage
            },
            function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.CHAT_UPDATED, data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.FAILED_TO_POST_MESSAGE, data);
            })

    };


    return {
        "createChat": _createChat,
        "addMember": _addMember,
        "removeMember": _removeMember,
        "postMessage": _postMessage
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return ChatService;
});

