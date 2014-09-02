'use strict';

entropyApp.controller('ThreadsController', ['$scope', '$routeParams', 'Thread',
    function ($scope, $routeParams, Thread) {

        $scope.refresh = function () {
            Thread.query({}, function (threads) {
                $scope.threads = threads;
            });
        };

        $scope.refresh();

        $scope.delete = function (id) {
            Thread.delete({id: id},
                function () {
                    $scope.threads = Thread.query();
                });
        };

        $scope.clear = function () {
            $scope.thread = {id: null, uri: null, title: null, description: null};
        };
    }]);
