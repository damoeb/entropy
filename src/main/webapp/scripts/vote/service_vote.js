'use strict';

entropyApp.factory('Vote', ['$resource',
    function ($resource) {
        return $resource('app/rest/votes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
