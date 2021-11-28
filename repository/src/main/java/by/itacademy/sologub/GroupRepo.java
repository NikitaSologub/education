package by.itacademy.sologub;

import java.util.List;

public interface GroupRepo {
    List<Group> getGroups();

    List<Group> getGroupsByTeacher(Teacher teacher);

    List<Group> getGroupsByStudent(Student student);

    Group getGroupById(int id);

    boolean putGroupIfNotExists(Group group);

    boolean changeGroupsParametersIfExists(Group group);

    boolean addStudentInGroup(Group group,Student student);

    boolean removeStudentFromGroup(Group group,Student student);

    boolean addSubjectInGroup(Group group,Subject subject);

    boolean removeSubjectFromGroup(Group group,Subject subject);

    boolean deleteGroupIfExists(int groupId);
}