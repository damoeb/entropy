'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/thread/:id/reports', {
                    templateUrl: 'views/reports.html',
                    controller: 'ReportController',
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);