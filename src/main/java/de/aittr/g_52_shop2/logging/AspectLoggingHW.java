package de.aittr.g_52_shop2.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AspectLoggingHW {

    private Logger logger = LoggerFactory.getLogger(AspectLoggingHW.class);

    @Pointcut("execution(* de.aittr.g_52_shop2.service..*(..))")
    public void allServices() {
    }

    @Before("allServices()")
    public void beforeLoggingAllServices(JoinPoint joinPoint) {
        Object className = joinPoint.getTarget().getClass().getSimpleName();
        Object method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        logger.info("Method {} of the class {} was called with arguments {}", method, className, Arrays.toString(args));
    }

    @After("allServices()")
    public void afterLoggingAllServices(JoinPoint joinPoint) {
        Object className = joinPoint.getTarget().getClass().getSimpleName();
        Object method = joinPoint.getSignature().getName();
        logger.info("Method {} of the class {} finished its work", method, className);
    }

    @AfterReturning(
            pointcut = "allServices()",
            returning = "result"
    )
    public void afterReturningAllServices(JoinPoint joinPoint, Object result) {
        Object className = joinPoint.getTarget().getClass().getSimpleName();
        Object method = joinPoint.getSignature().getName();
        logger.info("Method {} of the class {} successfully returned {}", method, className, result);

    }

    @AfterThrowing(
            pointcut = "allServices()",
            throwing = "e"
    )
    public void afterThrowingAllServices(JoinPoint joinPoint, Exception e) {
        Object className = joinPoint.getTarget().getClass().getSimpleName();
        Object method = joinPoint.getSignature().getName();
        logger.warn("Method {} of the class {} threw an exception: {}", method, className, e.getMessage());
    }
}
