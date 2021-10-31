package by.itacademy.sologub;

import by.itacademy.sologub.role.Role;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Constants.*;
import static by.itacademy.sologub.constants.SqlQuery.*;

@Slf4j
public class TeacherRepoPostgresImpl extends AbstractPostgresRepo implements TeacherRepo {
    private static TeacherRepoPostgresImpl teacherRepo;
    private static CredentialRepoPostgresImpl credentialRepo;

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
        ResultSet rs = null;
        Teacher teacher = TEACHER_NOT_EXISTS;

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_TEACHER_BY_LOGIN)) {
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                teacher = extractTeacher(rs);
            }
        } catch (SQLException e) {
            log.error("Не удалось извлечь учителя из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return teacher;
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Teacher teacher = getTeacherIfExistsOrGetSpecialValue(login);

        if (TEACHER_NOT_EXISTS != teacher) {
            String dbPassword = teacher.getCredential().getPassword();

            if (password != null && password.equals(dbPassword)) {
                log.debug("Возвращаем учителя {} пароль={} верный", login, password);

            } else {
                teacher = TEACHER_PASSWORD_WRONG;
                log.debug("Не удалось взять учителя {} пароль={} не верный", login, password);
            }
        }
        return teacher;
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        int id = credentialRepo.putCredentialIfNotExistsAndGetId(teacher.getCredential());

        if (id == ID_NOT_EXISTS) {
            log.info("Нельзя добавить Учителя, такой логин уже существует");
        } else {
            teacher.getCredential().setId(id);

            try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(INSERT_TEACHER)) {
                st.setString(1, teacher.getFirstname());
                st.setString(2, teacher.getLastname());
                st.setString(3, teacher.getPatronymic());
                st.setDate(4, Date.valueOf(teacher.getDateOfBirth()));
                st.setInt(5, id);
                if (st.executeUpdate() > 0) {
                    log.info("Учитель {} добавлен бд", teacher);
                    return true;
                }
            } catch (SQLException e) {
                log.error("Не удалось совершить операцию добавления Учителя", e);
            }
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(String login) {
        Teacher teacher = teacherRepo.getTeacherIfExistsOrGetSpecialValue(login);
        return deleteTeacher(teacher);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        if (teacher != null && teacher != TEACHER_NOT_EXISTS) {
            try (Connection con = pool.getConnection();
                 PreparedStatement firstSt = con.prepareStatement(DELETE_TEACHER_BY_CREDENTIAL_ID);
                 PreparedStatement secondSt = con.prepareStatement(DELETE_CREDENTIAL_BY_ID)) {

                con.setAutoCommit(false);
                firstSt.setInt(1, teacher.getCredential().getId());
                secondSt.setInt(1, teacher.getCredential().getId());

                if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                    con.commit();
                    log.info("Учителя {} удалили из БД.", teacher);
                    return true;
                } else {
                    con.rollback();
                }
            } catch (SQLException e) {
                log.error("Не удалось совершить операцию удаления Учителя", e);
            }
        } else {
            log.info("Нельзя удалить Учителя {}. Логина не существует в базе", teacher);
        }
        return false;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher t) {
        try (Connection con = pool.getConnection();
             PreparedStatement firstSt = con.prepareStatement(UPDATE_TEACHER_BY_CREDENTIAL_ID);
             PreparedStatement secondSt = con.prepareStatement(UPDATE_CREDENTIAL_BY_ID)) {
            con.setAutoCommit(false);

            firstSt.setString(1, t.getFirstname());
            firstSt.setString(2, t.getLastname());
            firstSt.setString(3, t.getPatronymic());
            firstSt.setDate(4, Date.valueOf(t.getDateOfBirth()));
            firstSt.setInt(5, t.getCredential().getId());
            secondSt.setString(1, t.getCredential().getLogin());
            secondSt.setString(2, t.getCredential().getPassword());
            secondSt.setInt(3, t.getCredential().getId());

            if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                con.commit();
                log.info("Учителя {} изменили в БД.", t);
                return true;
            } else {
                con.rollback();
                log.info("не удалось изменить Учителя {} в БД.", t);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию узменения Учителя", e);
        }
        return false;
    }

    @Override
    public List<Teacher> getTeachersList() {
        List<Teacher> teachers = new ArrayList<>();

        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_TEACHERS_LIST);
             ResultSet rs = ps.executeQuery()) {

            teachers = extractTeachers(rs);
        } catch (SQLException e) {
            log.error("Не удалось извлечь список учителей из базы данных", e);
        }
        return teachers;
    }

    private List<Teacher> extractTeachers(ResultSet rs) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        log.debug("Создали пустой лист и переходим к извлечению данных");
        while (rs.next()) {
            Teacher t = extractTeacher(rs);
            log.debug("Извлекли обьект учителя {} добавляем его в список", t);
            teachers.add(t);
        }
        log.debug("Возвращаем список учителей");
        return teachers;
    }

    private Teacher extractTeacher(ResultSet rs) throws SQLException {
        return new Teacher()
                .withId(rs.getInt(ID))
                .withCredential(new Credential()
                        .withId(rs.getInt(CREDENTIAL_ID_DB_FIELD))
                        .withLogin(rs.getString(LOGIN))
                        .withPassword(rs.getString(PASSWORD)))
                .withFirstname(rs.getString(FIRSTNAME))
                .withLastname(rs.getString(LASTNAME))
                .withPatronymic(rs.getString(PATRONYMIC))
                .withDateOfBirth(rs.getDate(DATE_OF_BIRTH_DB_FIELD).toLocalDate())
                .withRole(Role.TEACHER);
    }
}