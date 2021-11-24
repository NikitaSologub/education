package by.itacademy.sologub;

import java.util.Set;

public interface StudentRepo {
    Set<Student> getStudentsSet();

    Set<Student> getStudentsByGroupId(int groupId);

    Student getStudentIfExistsOrGetSpecialValue(int id);

    Student getStudentIfExistsOrGetSpecialValue(String login);

    Student getStudentIfExistsOrGetSpecialValue(String login,String password);

    boolean putStudentIfNotExists(Student teacher);

    boolean changeStudentParametersIfExists(Student newStudent);

    boolean deleteStudent(String login);

    boolean deleteStudent(Student student);
}
