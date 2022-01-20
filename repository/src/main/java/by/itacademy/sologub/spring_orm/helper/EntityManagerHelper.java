package by.itacademy.sologub.spring_orm.helper;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class EntityManagerHelper {
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<>();
    private final SessionFactory factory;

    public EntityManager getEntityManager() {
        EntityManager em = ENTITY_MANAGER_CACHE.get();
        if (em == null || !em.isOpen()) {
            ENTITY_MANAGER_CACHE.set(em = factory.createEntityManager());
        }
        return em;
    }
}