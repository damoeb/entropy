'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/threads', {
                    templateUrl: 'views/threads.html',
                    controller: 'ThreadsController',
                    resolve: {
                        resolvedThread: ['Thread', function (Thread) {
                            return Thread.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
