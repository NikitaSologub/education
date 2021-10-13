package by.itacademy.sologub;

public interface StudentRepo {
    Student getStudentIfExistsOrGetSpecialValue(String login);

    Student getStudentIfExistsOrGetSpecialValue(String login,String password);

    boolean putStudentIfNotExists(Student teacher);
}
