'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/ban', {
                    templateUrl: 'views/bans.html',
                    controller: 'BanController',
                    resolve: {
                        resolvedBan: ['Ban', function (Ban) {
                            return Ban.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
