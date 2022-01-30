package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.model.Teacher;

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