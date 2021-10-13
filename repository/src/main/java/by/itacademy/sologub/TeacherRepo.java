package by.itacademy.sologub;

public interface TeacherRepo {
    Teacher getTeacherIfExistsOrGetSpecialValue(String login);

    Teacher getTeacherIfExistsOrGetSpecialValue(String login,String password);

    boolean putTeacherIfNotExists(Teacher teacher);
}
