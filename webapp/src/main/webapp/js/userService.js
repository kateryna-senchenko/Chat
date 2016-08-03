var UserService = function (eventbus, events, baseUrl) {

    var _registerUser = function (userData) {
        var username = userData.newNickname;
        var password = userData.newUserPassword;
        var confirmPassword = userData.newUserConfirmationPassword;
        $.post(baseUrl + "chat/registration",
            {
                username: username,
                password: password,
                confirmPassword: confirmPassword
            }, function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.REGISTRATION_IS_SUCCESSFUL, data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.REGISTRATION_FAILED, data);
            })
    };

    var _loginUser = function (userData) {
        var username = userData.nickname;
        var password = userData.password;
        $.post(baseUrl + "chat/login",
            {
                username: username,
                password: password
            },
            function (xhr) {
                var data = eval("(" + xhr + ")");
                eventbus.post(events.LOGIN_IS_SUCCESSFUL, data);
            }, 'text')
            .fail(function (xhr) {
                var data = eval("(" + xhr.responseText + ")");
                eventbus.post(events.LOGIN_FAILED, data);
            })
    };

    return {
        "addUser": _registerUser,
        "loginUser": _loginUser
    };
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}

define(function () {
    return UserService;
});

