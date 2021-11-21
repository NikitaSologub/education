package by.itacademy.sologub;

import java.util.Set;

public interface TeacherRepo {
    Set<Teacher> getTeachersList();

    Teacher getTeacherIfExistsOrGetSpecialValue(String login);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login,String password);

    boolean putTeacherIfNotExists(Teacher teacher);

    boolean changeTeachersParametersIfExists(Teacher newTeacher);

    boolean deleteTeacher(String login);

    boolean deleteTeacher(Teacher teacher);
}