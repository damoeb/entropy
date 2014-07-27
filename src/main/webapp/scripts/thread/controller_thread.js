'use strict';

entropyApp.controller('ThreadController', ['$scope', 'resolvedThread', 'Thread',
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

        $scope.voteUp = function (thread) {
            thread.likes++;
        };

        $scope.voteDown = function (thread) {
            thread.dislikes++;
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
