'use strict';

entropyApp.factory('Report', ['$resource',
    function ($resource) {
        return $resource('app/rest/reports/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    }]);
