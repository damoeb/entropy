package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Privilege;
import org.migor.entropy.repository.PrivilegeRepository;
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
 * REST controller for managing Privilege.
 */
@RestController
@RequestMapping("/app")
public class PrivilegeResource {

    private final Logger log = LoggerFactory.getLogger(PrivilegeResource.class);

    @Inject
    private PrivilegeRepository privilegeRepository;

    /**
     * POST  /rest/privileges -> Create a new privilege.
     */
    @RequestMapping(value = "/rest/privileges",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> create(@RequestBody Privilege privilege, HttpServletRequest request) {
        log.debug("REST request to save Privilege : {}", privilege);
        privilegeRepository.save(privilege);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * GET  /rest/privileges -> get all the privileges.
     */
    @RequestMapping(value = "/rest/privileges",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Privilege> getAll(HttpServletRequest request) {
        log.debug("REST request to get all Privileges");
        return privilegeRepository.findAll();
    }

    /**
     * GET  /rest/privileges/:id -> get the "id" privilege.
     */
    @RequestMapping(value = "/rest/privileges/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Privilege> get(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to get Privilege : {}", id);
        Privilege privilege = privilegeRepository.findOne(id);
        if (privilege == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(privilege, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/privileges/:id -> delete the "id" privilege.
     */
    @RequestMapping(value = "/rest/privileges/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> delete(@PathVariable Long id, HttpServletRequest request) {
        log.debug("REST request to delete Privilege : {}", id);
        privilegeRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
