package by.itacademy.sologub.postgres;

import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
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
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_STUDENT_SET_BY_GROUP_ID;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StudentRepoPostgresImpl extends AbstractUserPostgresRepo<Student> implements StudentRepo {
    @Override
    public Set<Student> getStudentsSet() {
        return getUsersSet();
    }

    @Override
    public Set<Student> getStudentsByGroupId(int groupId) {
        Set<Student> students = new HashSet<>();
        ResultSet set = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_STUDENT_SET_BY_GROUP_ID)) {
            st.setInt(1, groupId);
            set = st.executeQuery();

            while (set.next()) {
                students.add(extractObject(set));
            }
            log.info("Извлекли все Subjects из БДпо groupId={}", groupId);
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения Subject set по groupId=" + groupId, e);
        } finally {
            closeResource(set);
        }
        return students;
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
    public boolean changeStudentParametersIfExists(Student s) {
        return changeUsersParametersIfExists(s);
    }

    @Override
    public boolean deleteStudent(String login) {
        Student s = getStudentIfExistsOrGetSpecialValue(login);
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
