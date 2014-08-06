'use strict';

entropyApp.factory('Comment', ['$resource',
    function ($resource) {
        return $resource('app/rest/comments/:id/:action', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'like': { method: 'GET', params: {action: 'like'}},
            'dislike': { method: 'GET', params: {action: 'dislike'}},
            'flag': { method: 'GET', params: {action: 'flag'}}
        });
    }]);
