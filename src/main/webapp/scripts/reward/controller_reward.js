'use strict';

entropyApp.controller('RewardController', ['$scope', 'resolvedReward', 'Reward',
    function ($scope, resolvedReward, Reward) {

        $scope.rewards = resolvedReward;

        $scope.create = function () {
            Reward.save($scope.reward,
                function () {
                    $scope.rewards = Reward.query();
                    $('#saveRewardModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.reward = Reward.get({id: id});
            $('#saveRewardModal').modal('show');
        };

        $scope.delete = function (id) {
            Reward.delete({id: id},
                function () {
                    $scope.rewards = Reward.query();
                });
        };

        $scope.clear = function () {
            $scope.reward = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
