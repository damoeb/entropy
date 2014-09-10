'use strict';

entropyApp.factory('Punishment', ['$resource',
    function ($resource) {
        return $resource('app/rest/punishments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
