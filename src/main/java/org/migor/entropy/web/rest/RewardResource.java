package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Reward;
import org.migor.entropy.repository.RewardRepository;
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
 * REST controller for managing Reward.
 */
@RestController
@RequestMapping("/app")
public class RewardResource {

    private final Logger log = LoggerFactory.getLogger(RewardResource.class);

    @Inject
    private RewardRepository rewardRepository;

    /**
     * POST  /rest/rewards -> Create a new reward.
     */
    @RequestMapping(value = "/rest/rewards",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Reward reward) {
        log.debug("REST request to save Reward : {}", reward);
        rewardRepository.save(reward);
    }

    /**
     * GET  /rest/rewards -> get all the rewards.
     */
    @RequestMapping(value = "/rest/rewards",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Reward> getAll() {
        log.debug("REST request to get all Rewards");
        return rewardRepository.findAll();
    }

    /**
     * GET  /rest/rewards/:id -> get the "id" reward.
     */
    @RequestMapping(value = "/rest/rewards/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Reward> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Reward : {}", id);
        Reward reward = rewardRepository.findOne(id);
        if (reward == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reward, HttpStatus.OK);
    }

    /**
     * DELETE  /rest/rewards/:id -> delete the "id" reward.
     */
    @RequestMapping(value = "/rest/rewards/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Reward : {}", id);
        rewardRepository.delete(id);
    }
}
