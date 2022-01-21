package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
@Repository
public class SubjectRepoSpringOrmImpl extends AbstractSpringOrm<Subject> implements SubjectRepo {
    @Autowired
    public SubjectRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Subject.class, SUBJECT_NOT_EXISTS);
    }

    @Override
    @JpaTransaction
    public List<Subject> getSubjectsList() {
        List<Subject> subjects = findAll();
        log.debug("Достали {} из бд List<Subject>", subjects);
        if (subjects == null) return new ArrayList<>();
        return subjects;
    }

    @Override
    @JpaTransaction
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        Group g = getGroupById(groupId);
        if (GROUP_NOT_EXISTS == g) {
            log.debug("Группы с id={} не существует в БД", groupId);
            return new HashSet<>();
        }
        Set<Subject> result = g.getSubjects();
        log.debug("Достали {} из бд set<Subject>", result);
        return result;
    }

    @Override
    @JpaTransaction
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return findOneByIdIfExists(id);
    }

    @Override
    @JpaTransaction
    public boolean putSubjectIfNotExists(Subject subject) {
        return inputIfNotExists(subject);
    }

    @Override
    @JpaTransaction
    public boolean changeSubjectsParametersIfExists(Subject subject) {
        return updateIfExists(subject);
    }

    @Override
    @JpaTransaction
    public boolean deleteSubject(Subject subject) {
        if (subject == null || subject.getId() <= 0) {
            log.info("нельзя удалить предмет которого нет в БД");
            return false;
        }
        EntityManager em = helper.getEntityManager();
        em.flush(); //1Убедимся что все изменения записаны в базу
        em.clear(); //2 отсоединим все от текущего PersistenceContext и удалим из кэша 1-го уровня
        deleteAllMarksBySubjectId(em, subject.getId());//3 удалим все Marks перед удалением Subject на которую они глядят
        em.remove(subject); //4 непосредственное удаление нужного Subject
        return !em.contains(subject);//5 проверяем результат операции
    }

    private void deleteAllMarksBySubjectId(EntityManager em, int id) {
        int markRows = em.createNamedQuery("deleteAllMarksBySubjectId")
                .setParameter(ID, id)
                .executeUpdate();
        log.debug("Удалили в БД {} Marks у которых subjectId={}", markRows, id);
    }

    private Group getGroupById(int groupId) {
        TypedQuery<Group> typedQuery = helper.getEntityManager()
                .createNamedQuery("getGroupById", Group.class)
                .setParameter(ID, groupId);
        Group group = typedQuery.getSingleResult();
        log.debug("Достали {} из бд", group);
        if (group == null) return GROUP_NOT_EXISTS;
        return group;
    }
}