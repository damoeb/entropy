'use strict';

entropyApp.factory('Privilege', ['$resource',
    function ($resource) {
        return $resource('app/rest/privileges/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
