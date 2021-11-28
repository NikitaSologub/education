package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
public class SubjectRepoHibernateImpl extends AbstractCrudRepoJpa<Subject> implements SubjectRepo {
    private static volatile SubjectRepoHibernateImpl instance;

    private SubjectRepoHibernateImpl(SessionFactory sf) {
        super(sf, Subject.class);
    }

    public static SubjectRepoHibernateImpl getInstance(SessionFactory sf) {
        if (instance == null) {
            synchronized (SubjectRepoHibernateImpl.class) {
                if (instance == null) {
                    instance = new SubjectRepoHibernateImpl(sf);
                }
            }
        }
        return instance;
    }

    @Override
    protected Subject getEmptyObj() {
        return SUBJECT_NOT_EXISTS;
    }

    @Override
    public List<Subject> getSubjectsList() {
        return getAll();
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        //скорее всего что-то типо - сначала ищем группу по id а потом из нее берём все предметы
        return null;// дойду до групп - сделаю этот метод
    }

    @Override
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        Subject s = getByNamedQueryIntArgument("getSubjectById", id, ID);
        return getObjOrSpecialEmpty(s, String.valueOf(id), ID);
    }

    private Subject getObjOrSpecialEmpty(Subject s, String value, String columnName) {
        if (s == null) {
            log.debug("Не получилось получить обьект Subject по {}={}", columnName, value);
            return SUBJECT_NOT_EXISTS;
        }
        return s;
    }

    @Override
    public boolean putSubjectIfNotExists(Subject subject) {
        return input(subject);
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject subject) {
        return change(subject);
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        if (subject == null || subject.getId() <= 0) return false;
        int markRows;
        boolean result = false;
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.flush(); //1Убедимся что все изменения записаны в базу
            em.clear(); //2 отсоединим все от текущего PersistenceContext и удалим из кэша 1-го уровня
            Query query = em
                    .createNamedQuery("deleteAllMarksBySubjectId")
                    .setParameter(ID, subject.getId());
            markRows = query.executeUpdate(); //3 удалим все Marks перед удалением Subject на которую они глядят
            log.debug("Удалили в БД {} Marks у которых subjectId={}", markRows, subject.getId());
            System.err.println("Удалили в БД " + markRows + " Marks у которых subjectId=" + subject.getId());

            em.remove(subject); //4 непосредственное удаление нужного Subject
            result = em.contains(subject); //5 проверяем результат операции
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось удалить информацию по запросу", e);
        } finally {
            em.close();
        }
        return result;
    }
}