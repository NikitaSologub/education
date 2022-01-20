package by.itacademy.sologub.spring_orm.aspects;

import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class JpaTransactionalAspect {
    private final EntityManagerHelper helper;

    @SneakyThrows
    @Around("@annotation(by.itacademy.sologub.spring_orm.aspects.JpaTransaction)")
    public Object getAll(ProceedingJoinPoint jp) {
        log.debug("***Transaction : stage 1{}", jp.getSignature().getName());
        Object result = null;
        EntityManager em = null;
        try {
            em = helper.getEntityManager();
            log.debug("***Transaction : stage 2 helper.getEntityManager(){}", em);
            em.getTransaction().begin();
            log.debug("***Transaction : stage 3 em.getTransaction().begin();");

            result = jp.proceed();

            log.debug("***Transaction : stage 4 after proceedingJoinPoint.proceed();");
            em.getTransaction().commit();
            log.debug("***Transaction : stage 5 after em.getTransaction().commit()");
        } catch (Exception e) {
            safeRollback(em, e);
        } finally {
            quietCloseEntityManager(em);
        }
        log.debug("***Transaction : stage 6 the end of transaction{}", jp.getSignature().getName());
        return result;
    }

    private void safeRollback(EntityManager em, Exception e) {
        log.error(e.getMessage(), e);
        log.debug("***Transaction : stage safeRollback ");
        if (em != null) {
            em.getTransaction().rollback();
        }
        //suppress throw e;
    }

    private void quietCloseEntityManager(EntityManager em) {
        log.debug("***Transaction : stage quietCloseEntityManager ");
        if (em != null) {
            try {
                log.debug("***Transaction : stage em.close();");
                em.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                //suppress throw e;
            }
        }
    }
}