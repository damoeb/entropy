'use strict';

entropyApp.factory('Thread', ['$resource',
    function ($resource) {
        return $resource('app/rest/threads/:id/:filter', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'reports': { method: 'GET', params: {filter: 'reports'}}
        });
    }]);