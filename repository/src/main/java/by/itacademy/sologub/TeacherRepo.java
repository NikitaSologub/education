package by.itacademy.sologub;

import java.util.List;

public interface TeacherRepo {
    Teacher getTeacherIfExistsOrGetSpecialValue(String login);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login,String password);

    boolean putTeacherIfNotExists(Teacher teacher);

    boolean deleteTeacher(String login);

    public boolean changeTeachersParametersIfExists(Teacher newTeacher);

    List<Teacher> getTeachersList();
}
