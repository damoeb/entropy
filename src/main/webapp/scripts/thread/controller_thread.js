'use strict';

entropyApp.controller('ThreadsController', ['$scope', 'resolvedThread', 'Thread',
    function ($scope, resolvedThread, Thread) {

        $scope.threads = resolvedThread;

        $scope.create = function () {
            Thread.save($scope.thread,
                function () {
                    $scope.threads = Thread.query();
                    $('#saveThreadModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.thread = Thread.get({id: id});
            $('#saveThreadModal').modal('show');
        };

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

entropyApp.controller('ThreadController', ['$scope', '$routeParams', 'Thread',
    function ($scope, $routeParams, Thread) {

        $scope.thread = Thread.get({id: $routeParams.id});

    }]);
