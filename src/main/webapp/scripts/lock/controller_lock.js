'use strict';

entropyApp.controller('LockController', ['$scope', 'resolvedLock', 'Lock',
    function ($scope, resolvedLock, Lock) {

        $scope.locks = resolvedLock;

        $scope.create = function () {
            Lock.save($scope.lock,
                function () {
                    $scope.locks = Lock.query();
                    $('#saveLockModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.lock = Lock.get({id: id});
            $('#saveLockModal').modal('show');
        };

        $scope.delete = function (id) {
            Lock.delete({id: id},
                function () {
                    $scope.locks = Lock.query();
                });
        };

        $scope.clear = function () {
            $scope.lock = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
