'use strict';

entropyApp.controller('CreateThreadsController', ['$scope', '$location', '$rootScope', '$routeParams', 'Thread',
    function ($scope, $location, $rootScope, $routeParams, Thread) {

        $scope.thread = {
            title: $routeParams.title,
            uri: $routeParams.uri
        };

//        $rootScope.$apply(function() {
//            $location.search('');
//
//        });

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

    }]);
