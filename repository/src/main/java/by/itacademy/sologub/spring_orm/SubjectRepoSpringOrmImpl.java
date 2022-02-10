package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class SubjectRepoSpringOrmImpl extends AbstractSpringOrm<Subject> implements SubjectRepo {

    public SubjectRepoSpringOrmImpl() {
        super(Subject.class, SUBJECT_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Subject> getSubjectsList() {
        List<Subject> subjects = findAll();
        log.debug("Достали {} из бд List<Subject>", subjects);
        if (subjects == null) return new ArrayList<>();
        return subjects;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return findOneByIdIfExists(id);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject subject) {
        return inputIfNotExists(subject);
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject subject) {
        return updateIfExists(subject);
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        if (subject == null || subject.getId() <= 0) {
            log.info("нельзя удалить предмет которого нет в БД");
            return false;
        }
        Subject s = em.find(Subject.class, subject.getId());
        log.info("перед удалением {} ", s);
        deleteAllMarksBySubjectId(subject.getId());
        log.info("перед удалением Subject есть он в БД = {} ", em.contains(s));
        em.remove(s);
        boolean isRemoved = em.contains(s);
        log.info("после удаления Subject есть он в БД = {} ", isRemoved);
        return isRemoved;
    }

    private void deleteAllMarksBySubjectId(int id) {
        int markRows = em.createNamedQuery("deleteAllMarksBySubjectId")
                .setParameter(ID, id)
                .executeUpdate();
        log.debug("Удалили в БД {} Marks у которых subjectId={}", markRows, id);
    }

    private Group getGroupById(int groupId) {
        Group group = em.createNamedQuery("getGroupById", Group.class)
                .setParameter(ID, groupId)
                .getSingleResult();
        log.debug("Достали {} из бд", group);
        if (group == null) return GROUP_NOT_EXISTS;
        return group;
    }
}