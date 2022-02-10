package by.itacademy.sologub;

import by.itacademy.sologub.model.Student;

import java.util.Set;

public interface StudentRepo {
    Set<Student> getStudentsSet();

    Set<Student> getStudentsByGroupId(int groupId);

    Student getStudentIfExistsOrGetSpecialValue(int id);

    Student getStudentIfExistsOrGetSpecialValue(String login);

    Student getStudentIfExistsOrGetSpecialValue(String login,String password);

    boolean putStudentIfNotExists(Student student);

    boolean changeStudentParametersIfExists(Student newStudent);

    boolean deleteStudent(String login);

    boolean deleteStudent(Student student);
}
