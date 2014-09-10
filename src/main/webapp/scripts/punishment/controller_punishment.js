'use strict';

entropyApp.controller('PunishmentController', ['$scope', 'resolvedPunishment', 'Punishment',
    function ($scope, resolvedPunishment, Punishment) {

        $scope.punishments = resolvedPunishment;

        $scope.create = function () {
            Punishment.save($scope.punishment,
                function () {
                    $scope.punishments = Punishment.query();
                    $('#savePunishmentModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.punishment = Punishment.get({id: id});
            $('#savePunishmentModal').modal('show');
        };

        $scope.delete = function (id) {
            Punishment.delete({id: id},
                function () {
                    $scope.punishments = Punishment.query();
                });
        };

        $scope.clear = function () {
            $scope.punishment = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
