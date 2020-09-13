package ru.ayubdzhanov.disksharingapp.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.ayubdzhanov.disksharingapp.domain.Credential;

@Aspect
@Component
public class MainControllerAspect {
    private static final Logger logger = LoggerFactory.getLogger(MainControllerAspect.class);

    @Around("execution(* ru.ayubdzhanov.disksharingapp.controllers.MainController.*(..))")
    public ResponseEntity<?> logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Credential credential = ((Credential) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        logger.info("Method " + joinPoint.getSignature().getName() + "was started by" + credential.getUsername());
        ResponseEntity<?> responseEntity = (ResponseEntity<?>) joinPoint.proceed();
        logger.info("Method " + joinPoint.getSignature().getName() + " ended");
        return responseEntity;
    }
}
