'use strict';

entropyApp.factory('Ban', ['$resource',
    function ($resource) {
        return $resource('app/rest/bans/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
