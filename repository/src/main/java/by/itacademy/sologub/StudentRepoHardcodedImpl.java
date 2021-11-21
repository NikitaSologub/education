package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
public class StudentRepoHardcodedImpl extends AbstractUserHardcodedRepo<Student> implements StudentRepo {
    static int CURRENT_MAX_STUDENT_ID = 6789;
    private static volatile StudentRepoHardcodedImpl instance;

    private StudentRepoHardcodedImpl(CredentialRepo credentialRepo) {
        super(credentialRepo);
    }

    public static StudentRepoHardcodedImpl getInstance(CredentialRepo credentialRepo) {
        if (instance == null) {
            synchronized (StudentRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new StudentRepoHardcodedImpl(credentialRepo);
                }
            }
        }
        return instance;
    }

    @Override
    protected int getCurrentMaxIdAndIncrement() {
        return CURRENT_MAX_STUDENT_ID++;
    }

    @Override
    protected Student getNotExists() {
        return STUDENT_NOT_EXISTS;
    }

    @Override
    protected Student getPasswordWrong() {
        return STUDENT_PASSWORD_WRONG;
    }

    @Override
    protected String getType() {
        return STUDENT;
    }

    @Override
    public Set<Student> getStudentsSet() {
        return getUserSet();
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return getUserIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        return getUserIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putStudentIfNotExists(Student teacher) {
        return putUserIfNotExists(teacher);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        return changeUserParametersIfExists(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        return deleteUser(login);
    }

    @Override
    public boolean deleteStudent(Student student) {
        return deleteStudent(student.getCredential().getLogin());
    }
}