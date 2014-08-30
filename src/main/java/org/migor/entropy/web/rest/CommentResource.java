package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.CommentStatus;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.service.CommentService;
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
    private CommentService commentService;

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

        if (comment != null) {
            comment.setAuthorId(SecurityUtils.getCurrentLogin());
            comment.setDisplayName("Anonymous");
            comment.setStatus(CommentStatus.APPROVED); // todo default status depending on user trust

            commentService.create(comment);
        }
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
        Comment comment = commentService.get(id);
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
        commentService.delete(id);
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

        try {
            return new ResponseEntity<>(commentService.like(id), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
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

        try {
            return new ResponseEntity<>(commentService.dislike(id), HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
