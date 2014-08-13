'use strict';

entropyApp.factory('Lock', ['$resource',
    function ($resource) {
        return $resource('app/rest/locks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
