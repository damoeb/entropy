package org.migor.entropy.service;

import org.migor.entropy.repository.LockRepository;
import org.migor.entropy.web.rest.Once;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class DoormanService {

    private final Logger log = LoggerFactory.getLogger(DoormanService.class);

    @Inject
    private LockRepository lockRepository;

    /**
     * @param once
     * @return
     */
    public boolean knock(Once once) {
        // todo implement
        return true;
    }

    /**
     * @param once
     */
    public void enter(Once once) {
        // todo implement
    }
}
