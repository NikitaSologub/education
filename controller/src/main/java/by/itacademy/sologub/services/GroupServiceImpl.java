package by.itacademy.sologub.services;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GroupServiceImpl extends AbstractService implements GroupService {
    private static final String PREFIX = "groupRepo";
    private final Map<String, GroupRepo> repoMap;
    private volatile GroupRepo repo = null;

    @Autowired
    public GroupServiceImpl(Map<String, GroupRepo> repoMap) {
        this.repoMap = repoMap;
    }

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
    }

    @Override
    public List<Group> getGroups() {
        return repo.getGroups();
    }

    @Override
    public List<Group> getGroupsByTeacher(Teacher t) {
        return repo.getGroupsByTeacher(t);
    }

    @Override
    public List<Group> getGroupsByStudent(Student s) {
        return repo.getGroupsByStudent(s);
    }

    @Override
    public Group getGroupById(int id) {
        return repo.getGroupById(id);
    }

    @Override
    public boolean putGroupIfNotExists(Group g) {
        return repo.putGroupIfNotExists(g);
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group g) {
        return repo.changeGroupsParametersIfExists(g);
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        return repo.addStudentInGroup(group, student);
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        return repo.removeStudentFromGroup(group, student);
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        return repo.addSubjectInGroup(group, subject);
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        return repo.removeSubjectFromGroup(group, subject);
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        return repo.deleteGroupIfExists(groupId);
    }
}