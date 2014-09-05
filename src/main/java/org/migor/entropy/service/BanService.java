package org.migor.entropy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Transactional
public class BanService {

    private final Logger log = LoggerFactory.getLogger(BanService.class);

    public boolean isBannedUser(String currentLogin) {
        // todo implement
        return false;
    }

    public boolean isBannedRequest(HttpServletRequest request) {
        // todo implement
        return false;
    }
}
