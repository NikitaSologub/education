package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import static by.itacademy.sologub.constants.SqlQuery.DELETE_TEACHER_BY_CREDENTIAL_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_TEACHERS_LIST;
import static by.itacademy.sologub.constants.SqlQuery.GET_TEACHER_BY_LOGIN;
import static by.itacademy.sologub.constants.SqlQuery.INSERT_TEACHER;
import static by.itacademy.sologub.constants.SqlQuery.UPDATE_TEACHER_BY_CREDENTIAL_ID;

@Slf4j
public class TeacherRepoPostgresImpl extends AbstractUserPostgresRepo implements TeacherRepo {
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
        ResultSet rs = null;
        Teacher teacher = TEACHER_NOT_EXISTS;

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_TEACHER_BY_LOGIN)) {
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                teacher = extractObject(rs);
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
        return putUserIfNotExists(teacher, INSERT_TEACHER);
    }

    @Override
    public boolean deleteTeacher(String login) {
        Teacher teacher = teacherRepo.getTeacherIfExistsOrGetSpecialValue(login);
        return deleteTeacher(teacher);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return deleteUser(teacher, DELETE_TEACHER_BY_CREDENTIAL_ID, TEACHER_NOT_EXISTS);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher t) {
        return changeUsersParametersIfExists(t, UPDATE_TEACHER_BY_CREDENTIAL_ID);
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
            Teacher t = extractObject(rs);
            log.debug("Извлекли обьект учителя {} добавляем его в список", t);
            teachers.add(t);
        }
        log.debug("Возвращаем список учителей");
        return teachers;
    }

    @Override
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
}