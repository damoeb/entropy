package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.DoormanException;
import org.migor.entropy.domain.PrivilegeName;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
    @LimitFrequency(resource = "comment", freeze = 5, timeUnit = TimeUnit.SECONDS)
    @Privileged(PrivilegeName.SAVE_COMMENT)
    public ResponseEntity<Object> create(@RequestBody Comment comment, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to save Comment : {}", comment);

        if (comment != null) {
            comment.setAuthorId(SecurityUtils.getCurrentLogin());
            comment.setDisplayName("Anonymous");

            // todo use reputation service to get status
            comment.setStatus(Comment.Status.APPROVED);

            commentService.create(comment);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /rest/comments/:id -> get the "id" comment.
     */
    @RequestMapping(value = "/rest/comments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comment> get(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to get Comment : {}", id);
        Comment comment = commentService.get(id);
        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
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
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete Comment : {}", id);
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
