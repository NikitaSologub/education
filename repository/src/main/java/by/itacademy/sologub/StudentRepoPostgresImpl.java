package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.*;
import static by.itacademy.sologub.constants.ConstantObject.*;
import static by.itacademy.sologub.constants.SqlQuery.*;

@Slf4j
public class StudentRepoPostgresImpl extends AbstractUserPostgresRepo implements StudentRepo {
    private static StudentRepoPostgresImpl studentRepo;
    private static CredentialRepoPostgresImpl credentialRepo;

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
    public List<Student> getStudentsList() {
        List<Student> students = new ArrayList<>();

        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_STUDENTS_LIST);
             ResultSet rs = ps.executeQuery()) {

            students = extractStudents(rs);
        } catch (SQLException e) {
            log.error("Не удалось извлечь список учителей из базы данных", e);
        }
        return students;
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        ResultSet rs = null;
        Student student = STUDENT_NOT_EXISTS;

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_STUDENT_BY_LOGIN)) {
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                student = extractObject(rs);
            }
        } catch (SQLException e) {
            log.error("Не удалось извлечь учителя из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return student;
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Student student = getStudentIfExistsOrGetSpecialValue(login);

        if (STUDENT_NOT_EXISTS != student) {
            String dbPassword = student.getCredential().getPassword();

            if (password != null && password.equals(dbPassword)) {
                log.debug("Возвращаем студента {} пароль={} верный", login, password);

            } else {
                student = STUDENT_PASSWORD_WRONG;
                log.debug("Не удалось взять студента {} пароль={} не верный", login, password);
            }
        }
        return student;
    }

    private List<Student> extractStudents(ResultSet rs) throws SQLException {
        List<Student> students = new ArrayList<>();
        log.debug("Создали пустой лист и переходим к извлечению данных");
        while (rs.next()) {
            Student s = extractObject(rs);
            log.debug("Извлекли обьект студента {} добавляем его в список", s);
            students.add(s);
        }
        log.debug("Возвращаем список студентов");
        return students;
    }

    @Override
    public boolean putStudentIfNotExists(Student s) {
        return putUserIfNotExists(s, INSERT_STUDENT);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student s) {
        return changeUsersParametersIfExists(s, UPDATE_STUDENT_BY_CREDENTIAL_ID);
    }

    @Override
    public boolean deleteStudent(String login) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(login);
        return deleteStudent(s);
    }

    @Override
    public boolean deleteStudent(Student s) {
        return deleteUser(s, DELETE_STUDENT_BY_CREDENTIAL_ID, STUDENT_NOT_EXISTS);
    }

    @Override
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
}
