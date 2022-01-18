package by.itacademy.sologub.memory;

import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Slf4j
@Repository
public class TeacherRepoMemoryImpl extends AbstractUserMemoryRepo<Teacher> implements TeacherRepo {
    static int CURRENT_MAX_TEACHER_ID = 19051;

    @Autowired
    public TeacherRepoMemoryImpl(CredentialRepoMemoryImpl credentialRepo) {
        super(credentialRepo);
    }

    @Override
    protected int getCurrentMaxIdAndIncrement() {
        return CURRENT_MAX_TEACHER_ID++;
    }

    @Override
    protected Teacher getNotExists() {
        return TEACHER_NOT_EXISTS;
    }

    @Override
    protected Teacher getPasswordWrong() {
        return TEACHER_PASSWORD_WRONG;
    }

    @Override
    protected String getType() {
        return TEACHER;
    }

    @Override
    public Set<Teacher> getTeachersSet() {
        return getUserSet();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return getUserIfExistsOrGetSpecialValue(id);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        return getUserIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        return getUserIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        return putUserIfNotExists(teacher);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        return changeUserParametersIfExists(newTeacher);
    }

    @Override
    public boolean deleteTeacher(String login) {
        return deleteUser(login);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return deleteTeacher(teacher.getCredential().getLogin());
    }
}