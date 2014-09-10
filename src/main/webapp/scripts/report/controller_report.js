'use strict';

entropyApp.controller('ReportController', ['$scope', '$routeParams', '$log', 'Report', 'Thread',
    function ($scope, $routeParams, $log, Report, Thread) {

        var threadId = $routeParams.id;

        $scope.refresh = function () {

            Thread.reports({id: threadId}, function (response) {
                $scope.thread = response.thread;

                // todo group reports by comment
                $scope.reports = response.reports;
                $scope.comments = response.comments;

                _.each($scope.comments, function (comment) {
                    comment.pending = true;
                });

            });
        };

        $scope.refresh();

        $scope.approveClaimsOnComment = function (comment) {

            comment.pending = false;

            var reports = _.filter($scope.reports, function (report) {
                return report.commentId == comment.id;
            });
            _.each(reports, function (report) {
                Report.approve(report);
            });
        };

        $scope.rejectClaimsOnComment = function (comment) {

            comment.pending = false;

            var reports = _.filter($scope.reports, function (report) {
                return report.commentId == comment.id;
            });
            _.each(reports, function (report) {
                Report.reject(report);
            });
        };

    }]);
