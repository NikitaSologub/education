package by.itacademy.sologub;

import java.util.List;

public interface TeacherRepo {
    List<Teacher> getTeachersList();

    Teacher getTeacherIfExistsOrGetSpecialValue(String login);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login,String password);

    boolean putTeacherIfNotExists(Teacher teacher);

    boolean changeTeachersParametersIfExists(Teacher newTeacher);

    boolean deleteTeacher(String login);

    boolean deleteTeacher(Teacher teacher);
}