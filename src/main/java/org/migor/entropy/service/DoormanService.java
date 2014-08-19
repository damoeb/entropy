package org.migor.entropy.service;

import org.joda.time.DateTime;
import org.migor.entropy.domain.Lock;
import org.migor.entropy.repository.LockRepository;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.web.rest.Once;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

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
        Lock lock = lockRepository.findByGroupIdAndClientId(once.group(), SecurityUtils.getCurrentLogin());
        return lock == null || lock.getExpiration().isBeforeNow();
    }

    /**
     * @param once
     */
    public void enter(Once once) {

        Lock lock = lockRepository.findByGroupIdAndClientId(once.group(), SecurityUtils.getCurrentLogin());
        if (lock == null) {
            lock = new Lock();
        }
        lock.setClientId(SecurityUtils.getCurrentLogin());
        lock.setGroupId(once.group());
        DateTime expiration = DateTime.now().plus(TimeUnit.MILLISECONDS.convert(once.every(), once.timeUnit()));
        lock.setExpiration(expiration);

        lockRepository.save(lock);
    }
}
