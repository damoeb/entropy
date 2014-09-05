package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Lock;
import org.migor.entropy.domain.PrivilegeName;
import org.migor.entropy.repository.LockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller for managing Lock.
 */
@RestController
@RequestMapping("/app")
public class LockResource {

    private final Logger log = LoggerFactory.getLogger(LockResource.class);

    @Inject
    private LockRepository lockRepository;

    /**
     * GET  /rest/locks -> get all the locks.
     */
    @RequestMapping(value = "/rest/locks",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lock>> getAll(HttpServletRequest request) {
        log.debug("REST request to get all Locks");
        return new ResponseEntity<>(lockRepository.findAll(), HttpStatus.OK);
    }

    /**
     * GET  /rest/locks/:id -> get the "id" lock.
     */
    @RequestMapping(value = "/rest/locks/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lock> get(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get Lock : {}", id);
        Lock lock = lockRepository.findOne(id);
        if (lock == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(lock, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/locks/:id -> delete the "id" lock.
     */
    @RequestMapping(value = "/rest/locks/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.DELETE_LOCK)
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete Lock : {}", id);
        lockRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
