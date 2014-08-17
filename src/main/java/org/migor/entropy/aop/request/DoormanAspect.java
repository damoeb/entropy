package org.migor.entropy.aop.request;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.migor.entropy.service.DoormanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import javax.inject.Inject;
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
    public Object controlOnce(ProceedingJoinPoint joinPoint) throws Throwable {

        try {

            Signature signature = joinPoint.getSignature();

            Class[] paramTypes = new Class[joinPoint.getArgs().length];
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                paramTypes[i] = joinPoint.getArgs()[i].getClass();
            }

//            Method method = signature.getDeclaringType().getDeclaredMethod(signature.getName(), paramTypes);
//            Once once = method.getAnnotation(Once.class);
//
//            if(!doormanService.knock(once)) {
//                throw new IllegalAccessException("Request blocked temporarily");
//            }
//
//            doormanService.enter(once);

            Object result = joinPoint.proceed();

            return result;

        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());

            throw e;
        }
    }
}
