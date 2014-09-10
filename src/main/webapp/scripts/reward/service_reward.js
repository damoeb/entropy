'use strict';

entropyApp.factory('Reward', ['$resource',
    function ($resource) {
        return $resource('app/rest/rewards/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
