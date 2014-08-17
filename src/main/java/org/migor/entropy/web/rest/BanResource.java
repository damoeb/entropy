package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Ban;
import org.migor.entropy.repository.BanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Ban.
 */
@RestController
@RequestMapping("/app")
public class BanResource {

    private final Logger log = LoggerFactory.getLogger(BanResource.class);

    @Inject
    private BanRepository banRepository;

    /**
     * POST  /rest/bans -> Create a new ban.
     */
    @RequestMapping(value = "/rest/bans",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Ban ban) {
        log.debug("REST request to save Ban : {}", ban);
        banRepository.save(ban);
    }

    /**
     * GET  /rest/bans -> get all the bans.
     */
    @RequestMapping(value = "/rest/bans",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ban> getAll() {
        log.debug("REST request to get all Bans");
        return banRepository.findAll();
    }

    /**
     * GET  /rest/bans/:id -> get the "id" ban.
     */
    @RequestMapping(value = "/rest/bans/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ban> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Ban : {}", id);
        Ban ban = banRepository.findOne(id);
        if (ban == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ban, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/bans/:id -> delete the "id" ban.
     */
    @RequestMapping(value = "/rest/bans/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Ban : {}", id);
        banRepository.delete(id);
    }
}
