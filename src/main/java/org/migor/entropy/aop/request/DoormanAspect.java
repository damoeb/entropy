package org.migor.entropy.aop.request;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.migor.entropy.service.DoormanService;
import org.migor.entropy.web.rest.Once;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Aspect for controlling amount of request per user.
 */
@Aspect
public class DoormanAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private DoormanService doormanService;

    @Inject
    private Environment env;

    @Pointcut("within(org.migor.entropy.web.rest..*) && @annotation(org.migor.entropy.web.rest.Once)")
    public void enterPoincut() {
    }

    @Around("enterPoincut()")
    public Object guardOnce(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();

            Method method = signature.getMethod();
            Once once = method.getAnnotation(Once.class);

            if (!doormanService.knock(once)) {
                new ResponseEntity<>(HttpStatus.LOCKED);
            }

            doormanService.enter(once);

            return joinPoint.proceed();

        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
