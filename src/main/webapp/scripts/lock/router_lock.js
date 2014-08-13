'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/lock', {
                    templateUrl: 'views/locks.html',
                    controller: 'LockController',
                    resolve: {
                        resolvedLock: ['Lock', function (Lock) {
                            return Lock.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
