package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractCrudRepoJpa<T extends AbstractEntity> {
    private final SessionFactory sessionFactory;
    private final Class<T> objClass;

    protected AbstractCrudRepoJpa(SessionFactory sf, Class<T> objClass) {
        this.objClass = objClass;
        this.sessionFactory = sf;
    }

    protected abstract T getEmptyObj();

    protected EntityManager getEntityManager() {
        return sessionFactory.createEntityManager();
    }

    protected List<T> getAll() {
        List<T> result = new ArrayList<>();
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<T> query = manager.createQuery("from " + objClass.getName(), objClass);
            result.addAll(query.getResultList());
            log.debug("Достали List {} из бд", result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось достать List " + objClass.getSimpleName() + " из БД", e);
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected T get(int id) {
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        T result = getEmptyObj();
        try {
            transaction.begin();
            result = manager.find(objClass, id);
            log.debug("Достали {} из бд", result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось достать" + objClass.getSimpleName() + " из бд по id=" + id, e);
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected T getByNamedQueryStringArgument(String queryName, String arg, String columnName) {
        T result = getEmptyObj();
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<T> typedQuery = manager
                    .createNamedQuery(queryName, objClass)
                    .setParameter(columnName, arg);
            result = typedQuery.getSingleResult();
            log.debug("Достали {} из бд", result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось достать информацию по запросу", e);
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected T getByNamedQueryIntArgument(String queryName, int arg, String columnName) {
        T result = getEmptyObj();
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        try {
            transaction.begin();
            TypedQuery<T> typedQuery = manager
                    .createNamedQuery(queryName, objClass)
                    .setParameter(columnName, arg);
            result = typedQuery.getSingleResult();
            log.debug("Достали {} из бд", result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось достать информацию по запросу", e);
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected boolean input(T obj) {
        if (obj == null || obj.getId() != 0) return false;
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean result = false;
        try {
            transaction.begin();
            manager.persist(obj);
            result = manager.contains(obj);
            log.debug("Результат добавления{} = {} ", obj, result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не получилось добавить " + obj + ". Он имеет поля, которые уже есть у объекта в БД", e);
            transaction.rollback();
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected boolean change(T obj) {
        if (obj == null || obj.getId() <= 0) return false;
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean result = false;
        try {
            transaction.begin();
            obj = manager.merge(obj);
            result = manager.contains(obj);
            log.debug("Результат изменения{} = {} ", obj, result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не получилось изменить" + obj + "Он имеет поля, которые уже есть у объекта в БД", e);
            transaction.rollback();
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected boolean executeByOneStringParameter(String param, String paramName, String queryName) {
        if (param == null) return false;
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean result = false;
        try {
            transaction.begin();
            result = manager.createQuery(queryName)
                    .setParameter(paramName, param)
                    .executeUpdate() != 0;
            log.debug("Результат выполнения запроса с параметром {}={} будет {}", paramName, param, result);
        } catch (PersistenceException e) {
            log.error("Не получилось удалить объект по полю " + paramName + "=" + param, e);
            transaction.rollback();
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return result;
    }

    protected boolean remove(T obj) {
        if (obj == null || obj.getId() <= 0) return false;
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean result = false;
        try {
            transaction.begin();
            manager.remove(obj);
            result = manager.contains(obj);
            log.debug("Результат удаления{} = {} ", obj, result);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не получилось удалить " + obj + " в БД", e);
            transaction.rollback();
//            throw e;//TODO - убрать позже
        } finally {
            manager.close();
        }
        return !result;
    }
}