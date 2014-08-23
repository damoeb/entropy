package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.migor.entropy.domain.*;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ReportRepository;
import org.migor.entropy.repository.ThreadRepository;
import org.migor.entropy.repository.VoteRepository;
import org.migor.entropy.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for managing Comment.
 */
@RestController
@RequestMapping("/app")
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private ThreadRepository threadRepository;

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private ReportRepository reportRepository;

    /**
     * POST  /rest/comments -> Create a new comment.
     */
    @RequestMapping(value = "/rest/comments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Once(group = "post", every = 5, timeUnit = TimeUnit.SECONDS)
    public void create(@RequestBody Comment comment) {
        log.debug("REST request to save Comment : {}", comment);

        comment.setAuthorId(SecurityUtils.getCurrentLogin());
        comment.setDisplayName("Anonymous");
        comment.setStatus(CommentStatus.APPROVED); // todo default status depending on user trust

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

        commentRepository.save(comment);
    }

    /**
     * GET  /rest/comments/:id -> get the "id" comment.
     */
    @RequestMapping(value = "/rest/comments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comment> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Comment : {}", id);
        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/comments/:id -> delete the "id" comment.
     */
    @RequestMapping(value = "/rest/comments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        // todo check permissions
        commentRepository.delete(id);
    }

    /**
     * POST  /rest/comments/:id/like -> like the "id" comment.
     */
    @RequestMapping(value = "/rest/comments/{id}/like",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Once(group = "vote", every = 5, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Comment> like(@PathVariable Long id) {
        log.debug("REST request to like Comment : {}", id);

        Vote vote = voteRepository.findByCommentIdAndClientId(id, SecurityUtils.getCurrentLogin());
        if (vote != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (ThreadStatus.CLOSED == thread.getStatus()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


    /**
     * POST  /rest/comments/:id/dislike -> dislike the "id" comment.
     */
    @RequestMapping(value = "/rest/comments/{id}/dislike",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Once(group = "vote", every = 5, timeUnit = TimeUnit.SECONDS)
    public ResponseEntity<Comment> dislike(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to dislike Comment : {}", id);

        Comment comment = commentRepository.findOne(id);
        if (comment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (ThreadStatus.CLOSED == thread.getStatus()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        comment.setDislikes(comment.getDislikes() + 1);
        comment.setScore(comment.getLikes() - comment.getDislikes());
        commentRepository.save(comment);

        thread.setDislikes(thread.getDislikes() + 1);
        thread.setLastModifiedDate(DateTime.now());
        threadRepository.save(thread);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
}
