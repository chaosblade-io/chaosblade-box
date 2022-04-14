package com.alibaba.chaosblade.box.common.infrastructure.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * @author haibin
 *
 *
 */
@Aspect
@Configuration
@Slf4j
public class BeanMethodInvokeAdvice {

    @Around("@annotation(com.alibaba.chaosblade.box.common.common.annotation.TrackTime)")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;
        log.info("Time Taken by {} is {}", joinPoint, timeTaken);
    }

}
