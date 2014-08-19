'use strict';

entropyApp.factory('Comment', ['$resource',
    function ($resource) {
        return $resource('app/rest/comments/:id/:action', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'like': { method: 'POST', params: {action: 'like'}},
            'dislike': { method: 'POST', params: {action: 'dislike'}},
            'flag': { method: 'POST', params: {id: '@id', action: 'flag'}}
        });
    }]);
