'use strict';

entropyApp.controller('CreateThreadsController', ['$scope', '$location', '$rootScope', '$routeParams', 'Thread',
    function ($scope, $location, $rootScope, $routeParams, Thread) {

        $scope.thread = {
            title: $routeParams.title,
            uri: $routeParams.uri
        };

        $scope.create = function () {
            Thread.save($scope.thread,
                function () {
                    $scope.threads = Thread.query();
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.thread = {
                title: null,
                uri: null
            };
        };

    }]);
