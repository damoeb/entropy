'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/privilege', {
                    templateUrl: 'views/privileges.html',
                    controller: 'PrivilegeController',
                    resolve: {
                        resolvedPrivilege: ['Privilege', function (Privilege) {
                            return Privilege.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
