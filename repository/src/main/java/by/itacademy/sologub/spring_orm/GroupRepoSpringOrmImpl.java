package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;

@Slf4j
@Repository
public class GroupRepoSpringOrmImpl extends AbstractSpringOrm<Group> implements GroupRepo {
    @Autowired
    protected GroupRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Group.class, GROUP_NOT_EXISTS);
    }

    @Override
    @JpaTransaction
    public List<Group> getGroups() {
        List<Group> groups = findAll();
        log.info("возвращаем список всех гупп {}", groups);
        return groups;
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public List<Group> getGroupsByTeacher(Teacher teacher) {
        return null;
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public List<Group> getGroupsByStudent(Student student) {
        return null;
    }

    @Override
    @JpaTransaction
    public Group getGroupById(int id) {
        return getByNamedQueryIntArgument("getGroupById", id, ID);
    }

    @Override
    @JpaTransaction
    public boolean putGroupIfNotExists(Group group) {
        return inputIfNotExists(group);
    }

    @Override
    @JpaTransaction
    public boolean changeGroupsParametersIfExists(Group group) {
        return updateIfExists(group);
    }

    @Override
    @JpaTransaction
    public boolean addStudentInGroup(Group group, Student student) {
        group.addStudent(student);
        return updateIfExists(group);
    }

    @Override
    @JpaTransaction
    public boolean removeStudentFromGroup(Group group, Student student) {
        group.removeStudent(student);
        return updateIfExists(group);
    }

    @Override
    @JpaTransaction
    public boolean addSubjectInGroup(Group group, Subject subject) {
        group.addSubject(subject);
        return updateIfExists(group);
    }

    @Override
    @JpaTransaction
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        group.removeSubject(subject);
        return updateIfExists(group);
    }

    @Override
    @JpaTransaction
    public boolean deleteGroupIfExists(int groupId) {
        boolean result = helper.getEntityManager()
                .createNamedQuery("deleteGroupById")
                .setParameter(ID, groupId)
                .executeUpdate() > 0;
        log.debug("Удалили Group по groupId={}", groupId);
        return result;
    }
}