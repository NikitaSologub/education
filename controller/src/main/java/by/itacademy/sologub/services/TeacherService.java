package by.itacademy.sologub.services;

import by.itacademy.sologub.Teacher;

import java.util.Set;

public interface TeacherService {
    Set<Teacher> getTeachersSet();

    Teacher getTeacherIfExistsOrGetSpecialValue(int id);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login,String password);

    boolean putTeacherIfNotExists(Teacher teacher);

    boolean changeTeachersParametersIfExists(Teacher newTeacher);

    boolean deleteTeacher(String login);

    boolean deleteTeacher(Teacher teacher);
}