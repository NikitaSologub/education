package by.itacademy.sologub.services;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GroupServiceImpl implements GroupService {
    private static GroupServiceImpl teacherService;
    private final GroupRepo repo;

    private GroupServiceImpl(GroupRepo groupRepo) {
        this.repo = groupRepo;
    }

    public static GroupServiceImpl getInstance(GroupRepo groupRepo) {
        if (teacherService == null) {
            synchronized (GroupServiceImpl.class) {
                if (teacherService == null) {
                    teacherService = new GroupServiceImpl(groupRepo);
                }
            }
        }
        return teacherService;
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