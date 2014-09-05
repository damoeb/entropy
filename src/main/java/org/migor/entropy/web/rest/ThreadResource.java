package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.DoormanException;
import org.migor.entropy.domain.PrivilegeName;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.service.ThreadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * REST controller for managing Thread.
 */
@RestController
@RequestMapping("/app")
public class ThreadResource {

    private final Logger log = LoggerFactory.getLogger(ThreadResource.class);

    @Inject
    private ThreadService threadService;

    /**
     * POST  /rest/threads -> Create a new thread.
     */
    @RequestMapping(value = "/rest/threads",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @LimitFrequency(resource = "thread", freeze = 10, timeUnit = TimeUnit.MINUTES)
    @Privileged(PrivilegeName.CREATE_THREAD)
    public ResponseEntity<Object> create(@RequestBody Thread thread, HttpServletRequest request) {
        log.debug("REST request to save Thread : {}", thread);

        threadService.create(thread);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /rest/threads -> get all the threads.
     */
    @RequestMapping(value = "/rest/threads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Thread>> getAll(HttpServletRequest request) {
        log.debug("REST request to get all Threads");
        return new ResponseEntity<>(threadService.getAll(), HttpStatus.OK);
    }

    /**
     * GET  /rest/threads/:id -> get the "id" thread.
     */
    @RequestMapping(value = "/rest/threads/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to get Thread : {}", id);

        return new ResponseEntity<>(threadService.getDetailed(id), HttpStatus.OK);
    }

    /**
     * GET  /rest/threads/:id/reports -> get the reports for thread "id".
     */
    @RequestMapping(value = "/rest/threads/{id}/reports",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<String, Object>> reports(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to get reports of Thread : {}", id);

        return new ResponseEntity<>(threadService.getReports(id), HttpStatus.OK);
    }

    /**
     * DELETE  /rest/threads/:id -> delete the "id" thread.
     */
    @RequestMapping(value = "/rest/threads/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.DELETE_THREAD)
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete Thread : {}", id);
        threadService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
