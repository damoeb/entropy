'use strict';

entropyApp.factory('Comment', ['$resource',
    function ($resource) {
        return $resource('app/rest/comments/:id/:action', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
