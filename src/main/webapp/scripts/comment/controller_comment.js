'use strict';

entropyApp.controller('CommentController', ['$scope', '$routeParams', 'Thread', 'Comment', 'Report', '$log', '$location', '$anchorScroll',
    function ($scope, $routeParams, Thread, Comment, Report, $log, $location, $anchorScroll) {

        $scope.draft = {};
        $scope.reportModel = {};
        $scope.pendingCount = 0;
        $scope.commentCount = 0;
        $scope.reportCount = 0;

        var threadId = $routeParams.id;

        $scope.refresh = function () {

            Thread.get({id: threadId}, function (response) {
                $scope.thread = response.thread;
                $scope.commentCount = response.approved.length;
                $scope.approved = $scope.tree(response.approved);
                $scope.pendingCount = response.pendingCount;
                $scope.reportCount = response.reportCount;
            });
        };

        $scope.refresh();

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

        $scope.toggleReplyForm = function (comment) {

            $scope.draft.subject = comment.subject;
            comment.reply = !comment.reply;
            comment.report = false;
        };

        $scope.toggleReportForm = function (comment) {
            comment.reply = false;
            comment.report = !comment.report;
        };

        $scope.report = function (comment) {
            comment.report = false;

            $scope.reportModel.commentId = comment.id;

            Report.save($scope.reportModel,
                function () {
                    $scope.report.reason = null;
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
