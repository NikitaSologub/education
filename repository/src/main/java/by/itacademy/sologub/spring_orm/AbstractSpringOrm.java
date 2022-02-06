package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.model.AbstractEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public abstract class AbstractSpringOrm<T extends AbstractEntity> {
    protected final Class<T> type;
    protected final T emptyObj;
    @PersistenceContext(unitName = "entityManagerFactory")
    protected EntityManager em;

    protected List<T> findAll() {
        return em.createQuery("from " + type.getSimpleName(), type)
                .getResultList();
    }

    protected T findOneByIdIfExists(int id) {
        T entity = em.find(type, id);
        if (entity == null) {
            log.debug("Обьекта {} по id={} не существует в БД", type, id);
            return emptyObj;
        }
        return entity;
    }

    protected boolean inputIfNotExists(T obj) {
        if (obj == null || obj.getId() != 0) {
            log.debug("Нельзя вставить обьект который уже существует в БД или является специальным");
            return false;
        }
        em.persist(obj);
        boolean result = em.contains(obj);
        log.debug("Результат добавления{} = {} ", obj, result);
        return result;
    }

    protected boolean updateIfExists(T obj) {
        if (obj == null || obj.getId() <= 0) {
            log.debug("Нельзя обновить обьект которого нет в БД");
            return false;
        }
        obj = em.merge(obj);
        boolean result = em.contains(obj);
        log.debug("Результат изменения{} = {} ", obj, result);
        return result;
    }

    protected boolean removeIfExists(T entity) {
        if (entity == null || entity.getId() <= 0) {
            log.debug("Нельзя удалить обьект которого нет в БД");
            return false;
        }
        T object = em.find(type, entity.getId());
        log.debug("пытаемся удалить обьект {} из БД", object);
        em.remove(object);
        return !em.contains(object);
    }

    protected T getByNamedQueryIntArgument(String queryName, int arg, String columnName) {
        T result;
        if (arg <= 0) return emptyObj;
        try {
            result = em.createNamedQuery(queryName, type)
                    .setParameter(columnName, arg)
                    .getSingleResult();
        } catch (NoResultException e) {
            return emptyObj;
        }
        log.debug("Достали {} из бд", result);
        return result;
    }

    protected T getByNamedQueryStringArgument(String queryName, String arg, String columnName) {
        T result;
        try {
            result = em.createNamedQuery(queryName, type)
                    .setParameter(columnName, arg)
                    .getSingleResult();
        } catch (NoResultException e) {
            return emptyObj;
        }
        log.debug("Достали {} из бд", result);
        return result;
    }
}