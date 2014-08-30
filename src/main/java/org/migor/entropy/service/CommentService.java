package org.migor.entropy.service;

import org.joda.time.DateTime;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.domain.ThreadStatus;
import org.migor.entropy.domain.Vote;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ThreadRepository;
import org.migor.entropy.repository.VoteRepository;
import org.migor.entropy.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private ThreadRepository threadRepository;

    @Inject
    private VoteRepository voteRepository;


    public Comment create(Comment comment) {

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (thread == null) {
            throw new IllegalArgumentException("Invalid threadId");
        }
        if (ThreadStatus.CLOSED == thread.getStatus()) {
            throw new IllegalArgumentException("Already closed");
        }

        if (comment.getParentId() == null) {
            comment.setLevel(0);
        } else {
            Comment parent = commentRepository.findOne(comment.getParentId());

            if (parent.getThreadId() != thread.getId()) {
                throw new IllegalArgumentException("Invalid threadId");
            }

            comment.setLevel(parent.getLevel() + 1);
        }

        thread.setCommentCount(thread.getCommentCount() + 1);
        thread.setLastModifiedDate(DateTime.now());
        threadRepository.save(thread);

        return commentRepository.save(comment);
    }

    public Comment get(Long id) {
        return commentRepository.findOne(id);
    }

    public void delete(Long id) {
        // todo check permissions
        commentRepository.delete(id);
    }

    public Comment like(Long id) {

        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found");
        }

        Vote vote = voteRepository.findByCommentIdAndClientId(id, SecurityUtils.getCurrentLogin());
        if (vote != null) {
            throw new IllegalArgumentException("Already voted");
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (ThreadStatus.CLOSED == thread.getStatus()) {
            throw new IllegalArgumentException("Thread is closed");
        }

        comment.setLikes(comment.getLikes() + 1);
        comment.setScore(comment.getLikes() - comment.getDislikes());
        commentRepository.save(comment);

        thread.setLikes(thread.getLikes() + 1);
        thread.setLastModifiedDate(DateTime.now());
        threadRepository.save(thread);

        vote = new Vote();
        vote.setClientId(SecurityUtils.getCurrentLogin());
        vote.setCommentId(comment.getId());
        vote.setCreatedDate(DateTime.now());

        voteRepository.save(vote);

        return comment;
    }

    public Comment dislike(Long id) {
        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            throw new IllegalArgumentException("Comment not found");
        }

        Vote vote = voteRepository.findByCommentIdAndClientId(id, SecurityUtils.getCurrentLogin());
        if (vote != null) {
            throw new IllegalArgumentException("Already voted");
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (ThreadStatus.CLOSED == thread.getStatus()) {
            throw new IllegalArgumentException("Thread is closed");
        }

        comment.setDislikes(comment.getDislikes() + 1);
        comment.setScore(comment.getLikes() - comment.getDislikes());
        commentRepository.save(comment);

        thread.setDislikes(thread.getDislikes() + 1);
        thread.setLastModifiedDate(DateTime.now());
        threadRepository.save(thread);

        vote = new Vote();
        vote.setClientId(SecurityUtils.getCurrentLogin());
        vote.setCommentId(comment.getId());
        vote.setCreatedDate(DateTime.now());

        voteRepository.save(vote);

        return comment;
    }
}
