package by.itacademy.sologub.postgres;

import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.role.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_ALL_SALARIES_BY_TEACHER_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.EXCLUDE_TEACHER_FROM_ALL_GROUPS_BY_TEACHER_ID;

@Slf4j
@RequiredArgsConstructor
@Repository
public class TeacherRepoPostgresImpl extends AbstractUserPostgresRepo<Teacher> implements TeacherRepo {
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
        Teacher teacher = getTeacherIfExistsOrGetSpecialValue(login);
        return deleteTeacher(teacher);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        boolean isDeleted = deleteUser(teacher);
        if (isDeleted) {
            log.debug("Учитель по id={} уделён. Теперь удалим все его зарплаты", teacher.getId());

            try (Connection con = pool.getConnection();
                 PreparedStatement first = con.prepareStatement(DELETE_ALL_SALARIES_BY_TEACHER_ID);
                 PreparedStatement second = con.prepareStatement(EXCLUDE_TEACHER_FROM_ALL_GROUPS_BY_TEACHER_ID)) {
                first.setInt(1, teacher.getId());
                second.setInt(1, teacher.getId());

                int salaryNum = first.executeUpdate();
                log.debug("По teacherId={} было удалено salary в кол-ве {}", teacher.getId(), salaryNum);
                int groupNum = second.executeUpdate();
                log.debug("По teacherId={} учитель был отстранён из групп в кол-ве {}", teacher.getId(), groupNum);
            } catch (SQLException e) {
                log.error("Не удалось извлечь " + getRole() + " из базы данных", e);
            }
        }
        return isDeleted;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher t) {
        return changeUsersParametersIfExists(t);
    }

    @Override
    public Set<Teacher> getTeachersSet() {
        return getUsersSet();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return getUserIfExistsOrGetSpecialValue(id);
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