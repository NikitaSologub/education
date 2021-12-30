package by.itacademy.sologub.services;

import by.itacademy.sologub.CredentialRepoHardcodeImpl;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.StudentRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class StudentServiceImpl implements StudentService {
    private static StudentServiceImpl StudentService;
    private final StudentRepo repo;

    private StudentServiceImpl(StudentRepo StudentRepo) {
        this.repo = StudentRepo;
    }

    public static StudentServiceImpl getInstance(StudentRepo StudentRepo) {
        if (StudentService == null) {
            synchronized (StudentServiceImpl.class) {
                if (StudentService == null) {
                    StudentService = new StudentServiceImpl(StudentRepo);
                }
            }
        }
        return StudentService;
    }

    @Override
    public Set<Student> getStudentsSet() {
        return repo.getStudentsSet();
    }

    @Override
    public Set<Student> getStudentsByGroupId(int groupId) {
        return repo.getStudentsByGroupId(groupId);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return repo.getStudentIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return repo.getStudentIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        return repo.getStudentIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putStudentIfNotExists(Student t) {
        return repo.putStudentIfNotExists(t);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        return repo.changeStudentParametersIfExists(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        return repo.deleteStudent(login);
    }

    @Override
    public boolean deleteStudent(Student t) {
        return repo.deleteStudent(t);
    }
}