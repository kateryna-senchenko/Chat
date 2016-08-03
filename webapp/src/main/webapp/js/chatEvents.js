var ChatEvents = {

    ATTEMPT_TO_ADD_USER: 'attemptToAddUser',
    REGISTRATION_FAILED: 'registrationFailed',
    REGISTRATION_IS_SUCCESSFUL: 'registrationIsSuccessful',
    ATTEMPT_TO_RENDER_LOGIN_FORM: 'attemptToRenderLoginForm',

    ATTEMPT_TO_CREATE_CHAT: 'attemptToCreateChat',
    CHAT_CREATION_FAILED: 'chatCreationFailed',
    CHAT_IS_CREATED: 'chatIsCreated',
    CHAT_UPDATED: 'chatUpdated',

    ATTEMPT_TO_POST_MESSAGE: 'attemptToPostMessage',
    FAILED_TO_POST_MESSAGE: 'failedToPostMessage',

    ATTEMPT_TO_LOGIN_USER: 'attemptToLoginUser',
    LOGIN_FAILED: 'loginFailed',
    LOGIN_IS_SUCCESSFUL: 'loginIsSuccessful',

    ATTEMPT_TO_ADD_MEMBER: 'attemptToAddMember',
    MEMBER_IS_ADDED: 'memberIsAdded',
    FAILED_TO_ADD_MEMBER: 'failedToAddMember',

    ATTEMPT_TO_LEAVE_CHAT: 'attemptToLeaveChat',
    FAILED_TO_LEAVE_CHAT: 'failedToLeaveChat',
    MEMBER_HAS_LEFT: 'memberHasLeft'
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return ChatEvents;
});


