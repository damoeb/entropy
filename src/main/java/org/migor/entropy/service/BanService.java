package org.migor.entropy.service;

import org.joda.time.DateTime;
import org.migor.entropy.domain.Ban;
import org.migor.entropy.domain.BanType;
import org.migor.entropy.repository.BanRepository;
import org.migor.entropy.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
public class BanService {

    private final Logger log = LoggerFactory.getLogger(BanService.class);

    @Inject
    private BanRepository banRepository;

    public boolean isBannedUser(String currentLogin) {
        Ban ban = banRepository.findByTypeAndExpression(BanType.USER_ID, SecurityUtils.getCurrentLogin());
        return ban != null;
    }

    public boolean isBannedRequest(HttpServletRequest request) {
        List<Ban> ipBans = banRepository.findByTypeAndExpirationAfter(BanType.IP, DateTime.now());

//        new SubnetUtils(request.getRemoteAddr());

        // todo implement
        return false;
    }
}
