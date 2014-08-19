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
        $scope.draft = {};

        $scope.create = function () {

            $scope.draft.threadId = threadId;

            Comment.save($scope.draft,
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
            $scope.draft.subject = comment.subject;
            $scope.draft.parentId = comment.id;
        };

        $scope.flag = function (comment) {
            // todo ask for reason
            Comment.flag({id: comment.id, reason: 'the real reason'}, function () {
                $log.log('reported');
            });
        };

        $scope.like = function (comment) {
            comment.likes++;
            Comment.like({id: comment.id}, function (updated) {
                $log.log(updated);
                comment.like = updated.like;
            });
        };

        $scope.fullComment = function (comment) {
            return comment.score >= 0;
        };

        $scope.cancelRelyTo = function () {
            $scope.draft.parentId = null;
        };

        $scope.dislike = function (comment) {
            Comment.dislike({id: comment.id, up: false}, function (updated) {
                $log.log(updated);
                comment.dislikes = updated.dislikes;
            });
        };

//        $scope.delete = function (id) {
//            Comment.delete({id: id},
//                function () {
//                    $scope.comments = Comment.query();
//                });
//        };

        $scope.clear = function () {
            $scope.draft = {id: null, text: null};
        };
    }]);
