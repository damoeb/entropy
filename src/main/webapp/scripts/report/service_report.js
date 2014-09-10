'use strict';

entropyApp.factory('Report', ['$resource',
    function ($resource) {
        return $resource('app/rest/reports/:id/:action', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'},
            'reject': { method: 'POST', params: {action: 'reject'}},
            'approve': { method: 'POST', params: {action: 'approve'}}
        });
    }]);
