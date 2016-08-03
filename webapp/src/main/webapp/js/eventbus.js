var EventBus = function () {

    var _subscribers = {};

    var _post = function (eventType, evt) {

        var _specificSubscribers = _subscribers[eventType];

        if (typeof _specificSubscribers === 'undefined') {
            return;
        }

        for (var i = 0; i < _specificSubscribers.length; i++) {

            var _currentCallback = function (index) {

                var _currentCbk = _specificSubscribers[index];
                _currentCbk(evt);

            };

            _currentCallback(i);
        }

    };

    var _subscribe = function (eventType, callback) {

        if (typeof(callback) === "function") {

            if (typeof(_subscribers[eventType]) === "undefined") {
                _subscribers[eventType] = [];
            }

            _subscribers[eventType].push(callback);
        }
    };

    return {
        "post": _post,
        "subscribe": _subscribe
    }
};

if (typeof define !== 'function') {
    var define = require('amdefine')(module);
}


define(function () {
    return EventBus;
});
