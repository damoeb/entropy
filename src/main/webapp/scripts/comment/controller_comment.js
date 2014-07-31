'use strict';

entropyApp.controller('CommentController', ['$scope', '$routeParams', 'Thread', 'Comment',
    function ($scope, $routeParams, Thread, Comment) {

//        $scope.comments = resolvedComment;

        var threadId = $routeParams.id;
        $scope.thread = Thread.get({id: threadId});

        $scope.create = function () {

            $scope.comment.threadId = threadId;

            Comment.save($scope.comment,
                function () {
//                    $scope.comments = Comment.query();
                    $('#saveCommentModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.comment = Comment.get({id: id});
            $('#saveCommentModal').modal('show');
        };

//        $scope.delete = function (id) {
//            Comment.delete({id: id},
//                function () {
//                    $scope.comments = Comment.query();
//                });
//        };

        $scope.clear = function () {
            $scope.comment = {id: null, text: null};
        };
    }]);
