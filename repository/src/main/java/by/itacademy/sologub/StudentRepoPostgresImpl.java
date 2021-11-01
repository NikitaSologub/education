package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Constants.*;
import static by.itacademy.sologub.constants.SqlQuery.*;

@Slf4j
public class StudentRepoPostgresImpl extends AbstractPostgresRepo implements StudentRepo {
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
                student = extractStudent(rs);
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
            Student s = extractStudent(rs);
            log.debug("Извлекли обьект студента {} добавляем его в список", s);
            students.add(s);
        }
        log.debug("Возвращаем список студентов");
        return students;
    }

    private Student extractStudent(ResultSet rs) throws SQLException {
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
    public boolean putStudentIfNotExists(Student student) {
        int id = credentialRepo.putCredentialIfNotExistsAndGetId(student.getCredential());

        if (id == ID_NOT_EXISTS) {
            log.info("Нельзя добавить Студента, такой логин уже существует");
        } else {
            student.getCredential().setId(id);

            try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(INSERT_STUDENT)) {
                st.setString(1, student.getFirstname());
                st.setString(2, student.getLastname());
                st.setString(3, student.getPatronymic());
                st.setDate(4, Date.valueOf(student.getDateOfBirth()));
                st.setInt(5, id);
                if (st.executeUpdate() > 0) {
                    log.info("Студент {} добавлен бд", student);
                    return true;
                }
            } catch (SQLException e) {
                log.error("Не удалось совершить операцию добавления Студента", e);
            }
        }
        return false;
    }

    @Override
    public boolean changeStudentParametersIfExists(Student s) {
        try (Connection con = pool.getConnection();
             PreparedStatement firstSt = con.prepareStatement(UPDATE_STUDENT_BY_CREDENTIAL_ID);
             PreparedStatement secondSt = con.prepareStatement(UPDATE_CREDENTIAL_BY_ID)) {
            con.setAutoCommit(false);

            firstSt.setString(1, s.getFirstname());
            firstSt.setString(2, s.getLastname());
            firstSt.setString(3, s.getPatronymic());
            firstSt.setDate(4, Date.valueOf(s.getDateOfBirth()));
            firstSt.setInt(5, s.getCredential().getId());
            secondSt.setString(1, s.getCredential().getLogin());
            secondSt.setString(2, s.getCredential().getPassword());
            secondSt.setInt(3, s.getCredential().getId());

            if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                con.commit();
                log.info("Студента {} изменили в БД.", s);
                return true;
            } else {
                con.rollback();
                log.info("не удалось изменить Студента {} в БД.", s);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию узменения Студента", e);
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String login) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(login);
        return deleteStudent(s);
    }

    @Override
    public boolean deleteStudent(Student student) {
        if (student != null && student != STUDENT_NOT_EXISTS) {
            try (Connection con = pool.getConnection();
                 PreparedStatement firstSt = con.prepareStatement(DELETE_STUDENT_BY_CREDENTIAL_ID);
                 PreparedStatement secondSt = con.prepareStatement(DELETE_CREDENTIAL_BY_ID)) {

                con.setAutoCommit(false);
                firstSt.setInt(1, student.getCredential().getId());
                secondSt.setInt(1, student.getCredential().getId());

                if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                    con.commit();
                    log.info("Студента {} удалили из БД.", student);
                    return true;
                } else {
                    con.rollback();
                }
            } catch (SQLException e) {
                log.error("Не удалось совершить операцию удаления Студента", e);
            }
        } else {
            log.info("Нельзя удалить Студента {}. Логина не существует в базе", student);
        }
        return false;
    }
}
