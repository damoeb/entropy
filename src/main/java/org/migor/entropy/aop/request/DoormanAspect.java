package org.migor.entropy.aop.request;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.DoormanException;
import org.migor.entropy.domain.DoormanSummary;
import org.migor.entropy.domain.Privilege;
import org.migor.entropy.domain.PrivilegeName;
import org.migor.entropy.repository.PrivilegeRepository;
import org.migor.entropy.repository.UserRepository;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.service.BanService;
import org.migor.entropy.service.DoormanService;
import org.migor.entropy.web.rest.LimitFrequency;
import org.migor.entropy.web.rest.Privileged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Aspect for controlling amount of request per user.
 */
@Aspect
// todo disable FrequencyLimit on @Profile("dev")
public class DoormanAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private DoormanService doormanService;

    @Inject
    private BanService banService;

    @Inject
    private PrivilegeRepository privilegeRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private Environment env;

    @Pointcut("within(org.migor.entropy.web.rest..*)")
    public void enterPoincut() {
    }

    @Around("enterPoincut()")
    public Object guardOnce(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            final String userId = SecurityUtils.getCurrentLogin();
            final Method method = signature.getMethod();

            checkBans(joinPoint, signature, userId);

            checkPrivileges(userId, method);

            checkCallFrequency(method);

            return joinPoint.proceed();

        } catch (DoormanException e) {
            return new ResponseEntity<>(new DoormanSummary(e), HttpStatus.OK);
        }
    }

    private String checkBans(ProceedingJoinPoint joinPoint, MethodSignature signature, String userId) throws DoormanException {

        if (banService.isBannedUser(userId)) {
            throw new DoormanException(ErrorCode.BANNED);
        }

        HttpServletRequest request = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof HttpServletRequest) {
                request = (HttpServletRequest) arg;
            }
        }

        if (request == null) {
            log.warn(signature.getName() + " lacking the HttpServletRequest argument");

        } else if (banService.isBannedRequest(request)) {
            throw new DoormanException(ErrorCode.BANNED);
        }
        return userId;
    }

    private void checkPrivileges(String userId, Method method) throws DoormanException {
        Privileged privileged = method.getAnnotation(Privileged.class);
        if (privileged != null) {
            int reputationRequired = -1;
            for (PrivilegeName privilegeName : privileged.value()) {

                // find most restrictive
                Privilege privilege = privilegeRepository.findByName(privilegeName);
                if (reputationRequired < privilege.getReputation()) {
                    reputationRequired = privilege.getReputation();
                }
            }

            if (reputationRequired > userRepository.findOne(userId).getReputation()) {
                throw new DoormanException(ErrorCode.REPUTATION);
            }
        }
    }

    private void checkCallFrequency(Method method) throws DoormanException {
        LimitFrequency limitFrequency = method.getAnnotation(LimitFrequency.class);

        if (limitFrequency != null) {
            if (!doormanService.knock(limitFrequency)) {
                throw new DoormanException(ErrorCode.FREQUENCY);
            }
            doormanService.enter(limitFrequency);
        }
    }
}
