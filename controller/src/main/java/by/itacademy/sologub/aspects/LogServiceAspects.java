package by.itacademy.sologub.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class LogServiceAspects {
    @After("execution(* by.itacademy.sologub.services.*.*(..))")
    public void afterServiceMethod(JoinPoint jp) {
        log.info("service method {} success at {}", jp.getSignature().getName(), LocalDateTime.now());
    }
}