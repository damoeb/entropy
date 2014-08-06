'use strict';

entropyApp.controller('CommentController', ['$scope', '$routeParams', 'Thread', 'Comment', '$log',
    function ($scope, $routeParams, Thread, Comment, $log) {

        var threadId = $routeParams.id;

        $scope.refresh = function () {

            Thread.get({id: threadId}, function (response) {
                $scope.thread = response.thread;
                $scope.approved = $scope.tree(response.approved);
                $scope.pending = response.pending;
                $scope.rejected = response.rejected;
            });
        };


        $scope.refresh();
        $scope.comment = {};

        $scope.create = function () {

            $scope.comment.threadId = threadId;

            Comment.save($scope.comment,
                function () {
//                    $scope.comments = Comment.query();
//                    $('#saveCommentModal').modal('hide');
                    $scope.clear();

                    $scope.refresh();
                });
        };

        $scope.tree = function (comments) {

            var map = {};
            var roots = [];

            for (var i in comments) {
                var comment = comments[i];
                map[comment.id] = comment;
                comment.subcomments = [];
            }

            $.each(comments, function (index, comment) {
                if (comment.level == 0) {
                    roots.push(comment);
                } else {
                    var _parent = map[comment.parentId];
                    _parent.subcomments.push(comment);
                }
            });

            return roots;

        };

//        $scope.update = function (id) {
//            $scope.comment = Comment.get({id: id});
//            $('#saveCommentModal').modal('show');
//        };

        $scope.reply = function (comment) {
            $scope.comment.parentId = comment.id;
        };

        $scope.flag = function (comment) {
            Comment.flag({id: comment.id}, function () {
                $log.log('reported');
            });
        };

        $scope.voteUp = function (comment) {
            comment.likes++;
            Comment.vote({id: comment.id, up: true}, function () {
                $log.log('saved');
            });
        };

        $scope.voteDown = function (comment) {
            comment.dislikes++;
            Comment.vote({id: comment.id, up: false}, function () {
                $log.log('saved');
            });
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
