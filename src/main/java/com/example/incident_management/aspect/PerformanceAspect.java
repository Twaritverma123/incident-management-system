package com.example.incident_management.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("execution(* com.example.incident_management.controller..*(..))")
    public Object measureApiResponseTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {

            long duration = (System.nanoTime() - start) / 1_000_000;

            log.info(
                    "API: {} | Response Time: {} ms",
                    joinPoint.getSignature().toShortString(),
                    duration
            );
        }
    }
}