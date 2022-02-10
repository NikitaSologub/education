package by.itacademy.sologub.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Aspect
public class ExceptionAspect {
    @AfterThrowing(pointcut = "execution(* by.itacademy.sologub.controllers.*.*(..))", throwing = "e")
    public void loggingExceptionWithRethrowing(Exception e) throws Exception {
        log.error("Ошибка с слое контроллеров: {}", e.getMessage());
        throw e;
    }
}