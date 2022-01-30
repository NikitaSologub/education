package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class GroupRepoSpringOrmImpl extends AbstractSpringOrm<Group> implements GroupRepo {
    @Autowired
    protected GroupRepoSpringOrmImpl() {
        super(Group.class, GROUP_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Group getGroupById(int id) {
        return getByNamedQueryIntArgument("getGroupById", id, ID);
    }

    @Override
    public boolean putGroupIfNotExists(Group group) {
        return inputIfNotExists(group);
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group group) {
        return updateIfExists(group);
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        group.addStudent(student);
        return updateIfExists(group);
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        group.removeStudent(student);
        return updateIfExists(group);
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        group.addSubject(subject);
        return updateIfExists(group);
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        group.removeSubject(subject);
        return updateIfExists(group);
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        boolean result = em
                .createNamedQuery("deleteGroupById")
                .setParameter(ID, groupId)
                .executeUpdate() > 0;
        log.debug("Удалили Group по groupId={}", groupId);
        return result;
    }
}