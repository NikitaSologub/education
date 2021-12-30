package by.itacademy.sologub.services;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;

import java.util.List;

public interface GroupService {
    List<Group> getGroups();

    List<Group> getGroupsByTeacher(Teacher teacher);

    List<Group> getGroupsByStudent(Student student);

    Group getGroupById(int id);

    boolean putGroupIfNotExists(Group group);

    boolean changeGroupsParametersIfExists(Group group);

    boolean addStudentInGroup(Group group, Student student);

    boolean removeStudentFromGroup(Group group, Student student);

    boolean addSubjectInGroup(Group group, Subject subject);

    boolean removeSubjectFromGroup(Group group, Subject subject);

    boolean deleteGroupIfExists(int groupId);
}