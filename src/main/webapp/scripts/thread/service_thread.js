'use strict';

entropyApp.factory('Thread', ['$resource',
    function ($resource) {
        return $resource('app/rest/threads/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
