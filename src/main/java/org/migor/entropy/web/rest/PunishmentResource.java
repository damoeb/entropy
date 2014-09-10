package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Punishment;
import org.migor.entropy.repository.PunishmentRepository;
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
 * REST controller for managing Punishment.
 */
@RestController
@RequestMapping("/app")
public class PunishmentResource {

    private final Logger log = LoggerFactory.getLogger(PunishmentResource.class);

    @Inject
    private PunishmentRepository punishmentRepository;

    /**
     * POST  /rest/punishments -> Create a new punishment.
     */
    @RequestMapping(value = "/rest/punishments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Punishment punishment) {
        log.debug("REST request to save Punishment : {}", punishment);
        punishmentRepository.save(punishment);
    }

    /**
     * GET  /rest/punishments -> get all the punishments.
     */
    @RequestMapping(value = "/rest/punishments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Punishment> getAll() {
        log.debug("REST request to get all Punishments");
        return punishmentRepository.findAll();
    }

    /**
     * GET  /rest/punishments/:id -> get the "id" punishment.
     */
    @RequestMapping(value = "/rest/punishments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Punishment> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Punishment : {}", id);
        Punishment punishment = punishmentRepository.findOne(id);
        if (punishment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(punishment, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/punishments/:id -> delete the "id" punishment.
     */
    @RequestMapping(value = "/rest/punishments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Punishment : {}", id);
        punishmentRepository.delete(id);
    }
}
