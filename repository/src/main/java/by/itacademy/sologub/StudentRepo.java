package by.itacademy.sologub;

import java.util.List;

public interface StudentRepo {
    public List<Student> getStudentsList();

    Student getStudentIfExistsOrGetSpecialValue(String login);

    Student getStudentIfExistsOrGetSpecialValue(String login,String password);

    boolean putStudentIfNotExists(Student teacher);

    boolean changeStudentParametersIfExists(Student newStudent);

    boolean deleteStudent(String login);

    boolean deleteStudent(Student student);
}
