define(function (require) {

    var EventBus = require('./eventbus');
    var eventBus = new EventBus();

    var chatEvents = require('./chatEvents');

    var baseUrl = 'http://localhost:8080/';

    var UserService = require('./userService');
    var userService = new UserService(eventBus, chatEvents, baseUrl);

    var ChatService = require('./chatService');
    var chatService = new ChatService(eventBus, chatEvents, baseUrl);

    var ChatRoom = require('./chatRoom');
    var chatRoom = new ChatRoom("chat-app", eventBus, chatEvents, userService, chatService);

});
