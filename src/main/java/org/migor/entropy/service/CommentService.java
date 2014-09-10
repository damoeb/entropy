package org.migor.entropy.service;

import org.joda.time.DateTime;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.DoormanException;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ThreadRepository;
import org.migor.entropy.repository.VoteRepository;
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

    @Inject
    private ReputationService reputationService;


    public Comment create(Comment comment) throws DoormanException {

        if (comment == null) {
            throw new IllegalArgumentException("comment is null");
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (thread == null) {
            throw new DoormanException(Thread.class, ErrorCode.RESOURCE_NOT_FOUND);
        }
        if (Thread.Status.CLOSED == thread.getStatus()) {
            throw new DoormanException(Thread.class, ErrorCode.INVALID_STATUS, "Already closed");
        }

        if (comment.getParentId() == null) {
            comment.setLevel(0);
        } else {
            Comment parent = commentRepository.findOne(comment.getParentId());

            if (parent.getThreadId() != thread.getId()) {
                throw new DoormanException(Thread.class, ErrorCode.INVALID_DATA, "ParentId is in wrong thread");
            }

            comment.setLevel(parent.getLevel() + 1);
        }

        reputationService.judge(comment);


        thread.setCommentCount(thread.getCommentCount() + 1);
        thread.setLastModifiedDate(DateTime.now());
        threadRepository.save(thread);

        return commentRepository.save(comment);
    }

    public Comment get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        return commentRepository.findOne(id);
    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        // todo check permissions
//        commentRepository.delete(id);
    }

}
