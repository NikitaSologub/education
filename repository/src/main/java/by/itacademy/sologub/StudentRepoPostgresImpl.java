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
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
public class StudentRepoPostgresImpl extends AbstractUserPostgresRepo<Student> implements StudentRepo {
    private static volatile StudentRepoPostgresImpl studentRepo;
    private static volatile CredentialRepoPostgresImpl credentialRepo;

    private StudentRepoPostgresImpl(ComboPooledDataSource pool, CredentialRepoPostgresImpl repo) {
        super(pool);
        credentialRepo = repo;
    }

    public static StudentRepoPostgresImpl getInstance(ComboPooledDataSource pool, CredentialRepoPostgresImpl credRepo) {
        if (studentRepo == null) {
            synchronized (CredentialRepoHardcodeImpl.class) {
                if (studentRepo == null) {
                    studentRepo = new StudentRepoPostgresImpl(pool, credRepo);
                }
            }
        }
        return studentRepo;
    }

    @Override
    public Set<Student> getStudentsSet() {
        return getUsersSet();
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return getUserIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        return getUserIfExistsOrGetSpecialValue(login,password);
    }

    @Override
    public boolean putStudentIfNotExists(Student s) {
        return putUserIfNotExists(s);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student s) {
        return changeUsersParametersIfExists(s);
    }

    @Override
    public boolean deleteStudent(String login) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(login);
        return deleteStudent(s);
    }

    @Override
    public boolean deleteStudent(Student s) {
        return deleteUser(s);
    }

    @Override//todo - вынести метод ниже
    protected Student extractObject(ResultSet rs) throws SQLException {
        return new Student()
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
        return String.valueOf(Role.STUDENT);
    }

    @Override
    protected Student getNotExists() {
        return STUDENT_NOT_EXISTS;
    }

    @Override
    protected Student getPasswordWrong() {
        return STUDENT_PASSWORD_WRONG;
    }
}
