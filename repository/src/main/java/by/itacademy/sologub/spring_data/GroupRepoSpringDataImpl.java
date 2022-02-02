package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.spring_data.crud.GroupDataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class GroupRepoSpringDataImpl implements GroupRepo {
    private final GroupDataAccess groupDao;

    @Autowired
    public GroupRepoSpringDataImpl(GroupDataAccess groupDao) {
        this.groupDao = groupDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroups() {
        return groupDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroupsByTeacher(Teacher teacher) {
        return groupDao.findAllByTeacher(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> getGroupsByStudent(Student student) {
        return groupDao.findAll().stream()
                .filter(group -> group.getStudents().contains(student))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Group getGroupById(int id) {
        return groupDao.findById(id).orElse(GROUP_NOT_EXISTS);
    }

    @Override
    public boolean putGroupIfNotExists(Group group) {
        log.debug("добавляем повую группу в БД {}", group);
        Group groupInDb = groupDao.findByTitle(group.getTitle()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS == groupInDb) {
            return groupDao.save(group).getId() != 0;
        }
        return false;
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group group) {
        Group oldGroupInDb = groupDao.findById(group.getId()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != oldGroupInDb) {
            log.debug("корректируем параметры группы на новые {}", group);
            oldGroupInDb.setTitle(group.getTitle());
            oldGroupInDb.setDescription(group.getDescription());
            oldGroupInDb.setTeacher(group.getTeacher());
            Group newGroupInDb = groupDao.save(oldGroupInDb);
            return newGroupInDb.equals(oldGroupInDb);
        }
        return false;
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        Group oldGroupInDb = groupDao.findById(group.getId()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != oldGroupInDb) {
            log.debug("Группа существует, пытаемся добавить в неё студента {}", student);
            boolean isAdded = oldGroupInDb.getStudents().add(student);//без доставания из репозитория
            log.debug("Студент добавлен в группу={}", isAdded);
            Group newGroupInDb = groupDao.save(oldGroupInDb);
            return newGroupInDb.equals(oldGroupInDb);
        }
        return false;
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        Group oldGroupInDb = groupDao.findById(group.getId()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != oldGroupInDb) {
            log.debug("Группа существует, пытаемся исключить из неё студента {}", student);
            boolean isExcluded = oldGroupInDb.getStudents().remove(student);//без доставания из репозитория
            log.debug("Студент исключён из группы={}", isExcluded);
            Group newGroupInDb = groupDao.save(oldGroupInDb);
            return newGroupInDb.equals(oldGroupInDb);
        }
        return false;
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        Group oldGroupInDb = groupDao.findById(group.getId()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != oldGroupInDb) {
            log.debug("Группа существует, пытаемся добавить в неё предмет {}", subject);
            boolean isAdded = oldGroupInDb.getSubjects().add(subject);//без доставания из репозитория
            log.debug("Предмет добавлен в группу={}", isAdded);
            Group newGroupInDb = groupDao.save(oldGroupInDb);
            return newGroupInDb.equals(oldGroupInDb);
        }
        return false;
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        Group oldGroupInDb = groupDao.findById(group.getId()).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != oldGroupInDb) {
            log.debug("Группа существует, пытаемся исключить из неё предмет {}", subject);
            boolean isExcluded = oldGroupInDb.getSubjects().remove(subject);//без доставания из репозитория
            log.debug("Предмет исключён из группы={}", isExcluded);
            Group newGroupInDb = groupDao.save(oldGroupInDb);
            return newGroupInDb.equals(oldGroupInDb);
        }
        return false;
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        groupDao.deleteById(groupId);
        return !groupDao.existsById(groupId);
    }
}