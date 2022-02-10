package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;

@Slf4j
@Repository
public class GroupRepoHibernateImpl extends AbstractCrudRepoJpa<Group> implements GroupRepo {
    @Autowired
    public GroupRepoHibernateImpl(SessionFactory sf) {
        super(sf, Group.class);
    }

    @Override
    protected Group getEmptyObj() {
        return GROUP_NOT_EXISTS;
    }

    @Override
    public List<Group> getGroups() {
        return getAll();
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public List<Group> getGroupsByTeacher(Teacher teacher) {
        //подсказка: jp-ql select g from Group g where g.teacher.id=:id;
        return null;// сделать когда буду реализовывать view для учителей
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public List<Group> getGroupsByStudent(Student student) {
        return null;// сделать когда буду реализовывать view для студентов
    }

    @Override
    public Group getGroupById(int id) {
        return getByNamedQueryIntArgument("getGroupById", id, ID);
    }

    @Override
    public boolean putGroupIfNotExists(Group group) {
        return input(group);
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group group) {
        return change(group);
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        group.addStudent(student);
        return change(group);
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        group.removeStudent(student);
        return change(group);
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        group.addSubject(subject);
        return change(group);
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        group.removeSubject(subject);
        return change(group);
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        if (groupId <= 0) return false;
        boolean result = false;
        EntityManager em = getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Query query = em
                    .createNamedQuery("deleteGroupById")
                    .setParameter(ID, groupId);
            result = query.executeUpdate() > 0;
            log.debug("Удалили Group по groupId={}", groupId);
            transaction.commit();
        } catch (PersistenceException e) {
            log.error("Не удалось удалить информацию по запросу", e);
        } finally {
            em.close();
        }
        return result;
    }
}