package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.AbstractEntity;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public abstract class AbstractSpringOrm<T extends AbstractEntity> {
    protected final EntityManagerHelper helper;
    protected final Class<T> type;
    protected final T emptyObj;

    @Autowired
    protected AbstractSpringOrm(EntityManagerHelper helper, Class<T> type, T emptyObj) {
        this.helper = helper;
        this.type = type;
        this.emptyObj = emptyObj;
    }

    protected List<T> findAll() {
        return helper.getEntityManager()
                .createQuery("from " + type.getSimpleName(), type)
                .getResultList();
    }

    protected T findOneByIdIfExists(int id) {
        T entity = helper.getEntityManager().find(type, id);
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
        EntityManager em = helper.getEntityManager();
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
        EntityManager em = helper.getEntityManager();
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
        EntityManager em = helper.getEntityManager();
        em.remove(entity);
        return !em.contains(entity);
    }

    protected T getByNamedQueryIntArgument(String queryName, int arg, String columnName) {
        if (arg <= 0) return emptyObj;
        EntityManager em = helper.getEntityManager();
        TypedQuery<T> typedQuery = em
                .createNamedQuery(queryName, type)
                .setParameter(columnName, arg);
        T result = typedQuery.getSingleResult();
        log.debug("Достали {} из бд", result);
        if (result == null) return emptyObj;
        return result;
    }

    protected T getByNamedQueryStringArgument(String queryName, String arg, String columnName) {
        EntityManager manager = helper.getEntityManager();
        TypedQuery<T> typedQuery = manager
                .createNamedQuery(queryName, type)
                .setParameter(columnName, arg);
        T result = typedQuery.getSingleResult();
        log.debug("Достали {} из бд", result);
        if (result == null) return emptyObj;
        return result;
    }
}