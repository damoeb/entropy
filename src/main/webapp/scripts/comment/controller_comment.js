'use strict';

entropyApp.controller('CommentController', ['$scope', '$routeParams', 'Thread', 'Comment', '$log', '$location', '$anchorScroll',
    function ($scope, $routeParams, Thread, Comment, $log, $location, $anchorScroll) {

        var threadId = $routeParams.id;

        $scope.refresh = function () {

            Thread.get({id: threadId}, function (response) {
                $scope.thread = response.thread;
                $scope.approved = $scope.tree(response.approved);
                $scope.pendingCount = response.pendingCount;
                $scope.reportCount = response.reportCount;
            });
        };


        $scope.refresh();
        $scope.draft = {};
        $scope.pendingCount = 0;
        $scope.reportCount = 0;

        $scope.create = function () {

            $scope.draft.threadId = threadId;

            Comment.save($scope.draft,
                function () {
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
                comment.report = false;
                comment.maximized = true;
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

        $scope.reply = function (comment) {
            $scope.draft.subject = comment.subject;
            $scope.draft.parentId = comment.id;
        };

        $scope.report = function (comment, reason) {
            comment.report = false;
            Comment.report({id: comment.id, reason: reason}, function () {
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

        $scope.scrollTo = function (id) {
            var old = $location.hash();
            $location.hash(id);
            $anchorScroll();
            //reset to old to keep any additional routing logic from kicking in
            $location.hash(old);
        };

    }]);
