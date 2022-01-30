package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.SubjectRepo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
@Repository
public class SubjectRepoHibernateImpl extends AbstractCrudRepoJpa<Subject> implements SubjectRepo {
    private static volatile GroupRepoHibernateImpl groupRepo;

    @Autowired
    public SubjectRepoHibernateImpl(SessionFactory sf, GroupRepoHibernateImpl gr) {
        super(sf, Subject.class);
        groupRepo = gr;
    }

    @Override
    protected Subject getEmptyObj() {
        return SUBJECT_NOT_EXISTS;
    }

    @Override
    public List<Subject> getSubjectsList() {
        return getAll();
    }

    @Override
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        Group g = groupRepo.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == g) return new HashSet<>();
        return g.getSubjects();
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
        boolean result = false;
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.flush(); //1Убедимся что все изменения записаны в базу
            em.clear(); //2 отсоединим все от текущего PersistenceContext и удалим из кэша 1-го уровня
            deleteAllMarksBySubjectId(em,subject.getId());//3 удалим все Marks перед удалением Subject на которую они глядят
            em.remove(subject); //4 непосредственное удаление нужного Subject
            result = !em.contains(subject); //5 проверяем результат операции
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось удалить информацию по запросу", e);
        } finally {
            em.close();
        }
        return result;
    }

    private void deleteAllMarksBySubjectId(EntityManager em, int id) {
        int markRows = em.createNamedQuery("deleteAllMarksBySubjectId")
                .setParameter(ID, id)
                .executeUpdate();
        log.debug("Удалили в БД {} Marks у которых subjectId={}", markRows, id);
    }
}