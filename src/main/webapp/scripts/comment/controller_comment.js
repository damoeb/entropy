'use strict';

entropyApp.controller('CommentController', ['$scope', 'resolvedComment', 'Comment',
    function ($scope, resolvedComment, Comment) {

        $scope.comments = resolvedComment;

        $scope.create = function () {
            Comment.save($scope.comment,
                function () {
                    $scope.comments = Comment.query();
                    $('#saveCommentModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.comment = Comment.get({id: id});
            $('#saveCommentModal').modal('show');
        };

        $scope.delete = function (id) {
            Comment.delete({id: id},
                function () {
                    $scope.comments = Comment.query();
                });
        };

        $scope.clear = function () {
            $scope.comment = {id: null, sampleTextAttribute: null, sampleDateAttribute: null};
        };
    }]);
