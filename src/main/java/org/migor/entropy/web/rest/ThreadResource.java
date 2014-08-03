package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.CommentStatus;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.domain.ThreadStatus;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ThreadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Thread.
 */
@RestController
@RequestMapping("/app")
public class ThreadResource {

    private final Logger log = LoggerFactory.getLogger(ThreadResource.class);

    @Inject
    private ThreadRepository threadRepository;

    @Inject
    private CommentRepository commentRepository;

    /**
     * POST  /rest/threads -> Create a new thread.
     */
    @RequestMapping(value = "/rest/threads",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Thread thread) {
        log.debug("REST request to save Thread : {}", thread);

        thread.setStatus(ThreadStatus.OPEN);

        threadRepository.save(thread);
    }

    /**
     * GET  /rest/threads -> get all the threads.
     */
    @RequestMapping(value = "/rest/threads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Thread> getAll() {
        log.debug("REST request to get all Threads");
        return threadRepository.findAll();
    }

    /**
     * GET  /rest/threads/:id -> get the "id" thread.
     */
    @RequestMapping(value = "/rest/threads/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, HttpServletResponse response) {
//    public ResponseEntity<Thread> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Thread : {}", id);

        Map<String, Object> responseObj = new HashMap<>();

        Thread thread = threadRepository.findOne(id);
        if (thread == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        responseObj.put("thread", thread);
        responseObj.put("approved", commentRepository.findByThreadIdAndStatus(id, CommentStatus.APPROVED));
        responseObj.put("pending", commentRepository.findByThreadIdAndStatus(id, CommentStatus.PENDING));
        responseObj.put("rejected", commentRepository.findByThreadIdAndStatus(id, CommentStatus.REJECTED));
//        responseObj.put("spam", commentRepository.findByThreadIdAndStatus(id, CommentStatus.SPAM));
        return new ResponseEntity<>(responseObj, HttpStatus.OK);
//        return new ResponseEntity<>(thread, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/threads/:id -> delete the "id" thread.
     */
    @RequestMapping(value = "/rest/threads/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Thread : {}", id);
        threadRepository.delete(id);
    }
}
