package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
@Repository
public class StudentRepoMemoryImpl extends AbstractUserMemoryRepo<Student> implements StudentRepo {
    static int CURRENT_MAX_STUDENT_ID = 6789;
    private static volatile GroupRepoMemoryImpl groupRepo;

    @Autowired
    public StudentRepoMemoryImpl(CredentialRepoMemoryImpl credentialRepo, GroupRepoMemoryImpl groupRepo) {
        super(credentialRepo);
        StudentRepoMemoryImpl.groupRepo = groupRepo;
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
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return getUserIfExistsOrGetSpecialValue(id);
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
    public boolean putStudentIfNotExists(Student s) {
        return putUserIfNotExists(s);
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