'use strict';

entropyApp.controller('BanController', ['$scope', 'resolvedBan', 'Ban',
    function ($scope, resolvedBan, Ban) {

        $scope.bans = resolvedBan;

        $scope.create = function () {
            Ban.save($scope.ban,
                function () {
                    $scope.bans = Ban.query();
                    $('#saveBanModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.ban = Ban.get({id: id});
            $('#saveBanModal').modal('show');
        };

        $scope.delete = function (id) {
            Ban.delete({id: id},
                function () {
                    $scope.bans = Ban.query();
                });
        };

        $scope.clear = function () {
            $scope.ban = {id: null, type: null, reason: null, expression: null, expiration: null};
        };
    }]);
