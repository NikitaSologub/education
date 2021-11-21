package by.itacademy.sologub;

import by.itacademy.sologub.role.Role;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.CREDENTIAL_ID_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.DATE_OF_BIRTH_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Slf4j
public class TeacherRepoPostgresImpl extends AbstractUserPostgresRepo<Teacher> implements TeacherRepo {
    private static volatile TeacherRepoPostgresImpl teacherRepo;
    private static volatile CredentialRepoPostgresImpl credentialRepo;

    private TeacherRepoPostgresImpl(ComboPooledDataSource pool, CredentialRepoPostgresImpl repo) {
        super(pool);
        credentialRepo = repo;
    }

    public static TeacherRepoPostgresImpl getInstance(ComboPooledDataSource pool, CredentialRepoPostgresImpl credRepo) {
        if (teacherRepo == null) {
            synchronized (CredentialRepoHardcodeImpl.class) {
                if (teacherRepo == null) {
                    teacherRepo = new TeacherRepoPostgresImpl(pool, credRepo);
                }
            }
        }
        return teacherRepo;
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
    public boolean deleteTeacher(String login) {
        Teacher teacher = teacherRepo.getTeacherIfExistsOrGetSpecialValue(login);
        return deleteTeacher(teacher);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return deleteUser(teacher);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher t) {
        return changeUsersParametersIfExists(t);
    }

    @Override
    public Set<Teacher> getTeachersList() {
        return getUsersSet();
    }

    @Override//todo - вынести метод ниже
    protected Teacher extractObject(ResultSet rs) throws SQLException {
        return new Teacher()
                .withId(rs.getInt(ID))
                .withCredential(new Credential()
                        .withId(rs.getInt(CREDENTIAL_ID_DB_FIELD))
                        .withLogin(rs.getString(LOGIN))
                        .withPassword(rs.getString(PASSWORD)))
                .withFirstname(rs.getString(FIRSTNAME))
                .withLastname(rs.getString(LASTNAME))
                .withPatronymic(rs.getString(PATRONYMIC))
                .withDateOfBirth(rs.getDate(DATE_OF_BIRTH_DB_FIELD).toLocalDate());
    }

    @Override
    protected String getRole() {
        return String.valueOf(Role.TEACHER);
    }

    @Override
    protected Teacher getNotExists() {
        return TEACHER_NOT_EXISTS;
    }

    @Override
    protected Teacher getPasswordWrong() {
        return TEACHER_PASSWORD_WRONG;
    }
}