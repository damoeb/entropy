'use strict';

entropyApp.factory('Comment', ['$resource',
    function ($resource) {
        return $resource('app/rest/comments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
