'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/punishment', {
                    templateUrl: 'views/punishments.html',
                    controller: 'PunishmentController',
                    resolve: {
                        resolvedPunishment: ['Punishment', function (Punishment) {
                            return Punishment.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
