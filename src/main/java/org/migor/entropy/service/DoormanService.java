package org.migor.entropy.service;

import org.joda.time.DateTime;
import org.migor.entropy.domain.Lock;
import org.migor.entropy.repository.LockRepository;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.web.rest.LimitFrequency;
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
     * @param limitFrequency
     * @return false, iff lock is not expired
     */
    public boolean knock(LimitFrequency limitFrequency) {
        Lock lock = lockRepository.findByGroupIdAndAuthorId(limitFrequency.resource(), SecurityUtils.getCurrentLogin());
        return lock == null || lock.getExpiration().isBeforeNow();
    }

    /**
     * @param limitFrequency
     */
    public void enter(LimitFrequency limitFrequency) {

        Lock lock = lockRepository.findByGroupIdAndAuthorId(limitFrequency.resource(), SecurityUtils.getCurrentLogin());
        if (lock == null) {
            lock = new Lock();
        }
        lock.setAuthorId(SecurityUtils.getCurrentLogin());
        lock.setGroupId(limitFrequency.resource());
        DateTime expiration = DateTime.now().plus(TimeUnit.MILLISECONDS.convert(limitFrequency.freeze(), limitFrequency.timeUnit()));
        lock.setExpiration(expiration);

        lockRepository.save(lock);
    }
}
