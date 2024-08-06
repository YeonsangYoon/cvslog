package com.srpinfotec.cvslog.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;


@Slf4j
@Aspect
@Configuration
public class AopConfig {

    @Pointcut("execution(* com.srpinfotec.cvslog.repository.*Repository.*(..))")
    public void repositoryPointCut(){}

    @Around("repositoryPointCut()")
    public Object timeTrace(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();

        try{
            stopWatch.start();
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();

            long totalTime = stopWatch.getTotalTimeMillis();
            String methodName = joinPoint.getSignature().toShortString();

            log.debug("[{}] Method Execute Time : {} ms", methodName, totalTime);
        }
    }
}
