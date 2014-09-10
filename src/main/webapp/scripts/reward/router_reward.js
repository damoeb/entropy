'use strict';

entropyApp
    .config(['$routeProvider', '$httpProvider', '$translateProvider', 'USER_ROLES',
        function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/reward', {
                    templateUrl: 'views/rewards.html',
                    controller: 'RewardController',
                    resolve: {
                        resolvedReward: ['Reward', function (Reward) {
                            return Reward.query();
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        }]);
