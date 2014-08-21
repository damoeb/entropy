'use strict';

entropyApp.controller('ReportController', ['$scope', '$routeParams', 'Report', 'Thread',
    function ($scope, $routeParams, Report, Thread) {

        var threadId = $routeParams.id;

        $scope.refresh = function () {

            Thread.reports({id: threadId}, function (response) {
                $scope.thread = response.thread;

                // todo group reports by comment
                $scope.reports = response.reports;
                $scope.comments = response.comments;
            });
        };

        $scope.refresh();

        $scope.update = function (id) {
            $scope.report = Report.get({id: id});
            $('#saveReportModal').modal('show');
        };

    }]);
