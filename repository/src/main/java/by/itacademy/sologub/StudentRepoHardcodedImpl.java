package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
public class StudentRepoHardcodedImpl extends AbstractUserHardcodedRepo<Student> implements StudentRepo {
    static int CURRENT_MAX_STUDENT_ID = 6789;
    private static volatile StudentRepoHardcodedImpl instance;
    private static volatile GroupRepoHardcodedImpl groupRepo;

    private StudentRepoHardcodedImpl(CredentialRepo credentialRepo,GroupRepoHardcodedImpl groupRepo) {
        super(credentialRepo);
        StudentRepoHardcodedImpl.groupRepo = groupRepo;
    }

    public static StudentRepoHardcodedImpl getInstance(CredentialRepo credentialRepo,GroupRepoHardcodedImpl groupRepo) {
        if (instance == null) {
            synchronized (StudentRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new StudentRepoHardcodedImpl(credentialRepo,groupRepo);
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
    public Set<Student> getStudentsByGroupId(int groupId) {
        Group g = groupRepo.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == g) {
            log.debug("группа не существует - возвращаем пустой SET");
            return new HashSet<>();
        } else {
            log.debug("возвращаем Subject SET группы с groupId={}", groupId);
            return g.getStudents();
        }
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