package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.joda.time.DateTime;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.*;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.repository.CommentRepository;
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
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller for managing Vote.
 */
@RestController
@RequestMapping("/app")
public class VoteResource {

    private final Logger log = LoggerFactory.getLogger(VoteResource.class);

    @Inject
    private VoteRepository voteRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private ThreadRepository threadRepository;

    /**
     * POST  /rest/votes -> Create a new vote.
     */
    @RequestMapping(value = "/rest/votes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.SAVE_VOTE)
    public ResponseEntity<Object> create(@RequestBody Vote ref, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to save Vote : {}", ref);

        if (ref == null) {
            throw new DoormanException(Vote.class, ErrorCode.INVALID_DATA);
        }

        Comment comment = commentRepository.findOne(ref.getCommentId());
        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        Vote vote = voteRepository.findByCommentIdAndAuthorId(ref.getCommentId(), SecurityUtils.getCurrentLogin());
        if (vote != null) {
            throw new DoormanException(Vote.class, ErrorCode.ALREADY_EXISTS);
        }

        Thread thread = threadRepository.findOne(comment.getThreadId());
        if (Thread.Status.CLOSED == thread.getStatus()) {
            throw new DoormanException(Thread.class, ErrorCode.INVALID_STATUS, "Already closed");
        }

        // todo block votes from my comment

        if (ref.isLike()) {
            comment.setLikes(comment.getLikes() + 1);
            comment.setScore(comment.getLikes() - comment.getDislikes());
            thread.setLikes(thread.getLikes() + 1);

        } else {

            comment.setDislikes(comment.getDislikes() + 1);
            comment.setScore(comment.getLikes() - comment.getDislikes());
            thread.setDislikes(thread.getDislikes() + 1);
        }
        thread.setLastModifiedDate(DateTime.now());

        commentRepository.save(comment);
        threadRepository.save(thread);

        vote = new Vote();
        vote.setAuthorId(SecurityUtils.getCurrentLogin());
        vote.setCommentId(comment.getId());
        vote.setCreatedDate(DateTime.now());
        vote.setLike(ref.isLike());

        voteRepository.save(vote);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /rest/votes -> get all the votes.
     */
    @RequestMapping(value = "/rest/votes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Vote> getAll(HttpServletRequest request) {
        log.debug("REST request to get all Votes");
        return voteRepository.findAll();
    }

    /**
     * GET  /rest/votes/:id -> get the "id" vote.
     */
    @RequestMapping(value = "/rest/votes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Vote> get(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to get Vote : {}", id);
        Vote vote = voteRepository.findOne(id);
        if (vote == null) {
            throw new DoormanException(Vote.class, ErrorCode.RESOURCE_NOT_FOUND);
        }
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/votes/:id -> delete the "id" vote.
     */
    @RequestMapping(value = "/rest/votes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.DELETE_VOTE)
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete Vote : {}", id);
        voteRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
