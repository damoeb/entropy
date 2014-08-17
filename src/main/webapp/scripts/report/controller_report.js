'use strict';

entropyApp.controller('ReportController', ['$scope', 'resolvedReport', 'Report',
    function ($scope, resolvedReport, Report) {

        $scope.reports = resolvedReport;

        $scope.create = function () {
            Report.save($scope.report,
                function () {
                    $scope.reports = Report.query();
                    $('#saveReportModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.report = Report.get({id: id});
            $('#saveReportModal').modal('show');
        };

        $scope.delete = function (id) {
            Report.delete({id: id},
                function () {
                    $scope.reports = Report.query();
                });
        };

        $scope.clear = function () {
            $scope.report = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);