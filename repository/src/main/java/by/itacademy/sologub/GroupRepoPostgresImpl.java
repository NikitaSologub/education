package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.CREDENTIAL_ID_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.DATE_OF_BIRTH_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.DESCRIPTION;
import static by.itacademy.sologub.constants.Attributes.EXCLUDE;
import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.INCLUDE;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.Attributes.SUBJECT;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Attributes.TEACHER_ID_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.SqlQuery.CHANGE_GROUP_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.DELETE_GROUP_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.EXCLUDE_ALL_STUDENTS_FROM_GROUP_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.EXCLUDE_ALL_SUBJECTS_FROM_GROUP_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.EXCLUDE_STUDENT_FROM_GROUP_BY_IDS;
import static by.itacademy.sologub.constants.SqlQuery.EXCLUDE_SUBJECT_FROM_GROUP_BY_IDS;
import static by.itacademy.sologub.constants.SqlQuery.GET_GROUPS_LIST_BY_STUDENT_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_GROUPS_LIST_BY_TEACHER_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_GROUPS_LIST_WITH_TEACHERS;
import static by.itacademy.sologub.constants.SqlQuery.GET_GROUP_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.INCLUDE_STUDENT_IN_GROUP_BY_IDS;
import static by.itacademy.sologub.constants.SqlQuery.INCLUDE_SUBJECT_IN_GROUP_BY_IDS;
import static by.itacademy.sologub.constants.SqlQuery.SET_GROUP_RETURNING_GROUP_ID;

@Slf4j
public class GroupRepoPostgresImpl extends AbstractPostgresRepo<Group> implements GroupRepo {
    private static volatile GroupRepoPostgresImpl instance;

    private GroupRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    public static GroupRepoPostgresImpl getInstance(ComboPooledDataSource pool) {
        if (instance == null) {
            synchronized (GroupRepoPostgresImpl.class) {
                if (instance == null) {
                    instance = new GroupRepoPostgresImpl(pool);
                }
            }
        }
        return instance;
    }

    @Override
    protected Group extractObject(ResultSet set) throws SQLException {
        return new Group()
                .withId(set.getInt(ID))
                .withTitle(set.getString(TITLE))
                .withTeacher(extractTeacherFromRawRow(set))
                .withDescription(set.getString(DESCRIPTION));
    }

    private Teacher extractTeacherFromRawRow(ResultSet set) throws SQLException {
        int teacherId = set.getInt(TEACHER_ID_DB_FIELD);
        if (teacherId == 0) {
            return null;
        } else {
            return new Teacher()
                    .withId(teacherId)
                    .withCredential(new Credential()
                            .withId(set.getInt(CREDENTIAL_ID_DB_FIELD))
                            .withLogin(set.getString(LOGIN))
                            .withPassword(set.getString(PASSWORD)))
                    .withFirstname(set.getString(FIRSTNAME))
                    .withLastname(set.getString(LASTNAME))
                    .withPatronymic(set.getString(PATRONYMIC))
                    .withDateOfBirth(set.getDate(DATE_OF_BIRTH_DB_FIELD).toLocalDate());
        }
    }

    @Override
    public List<Group> getGroups() {
        List<Group> groups = new ArrayList<>();

        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_GROUPS_LIST_WITH_TEACHERS);
             ResultSet set = st.executeQuery()) {

            groups.addAll(extractObjects(set));
            log.debug("Получили из БД group list with teachers");
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения group list with teachers", e);
        }
        return groups;
    }

    @Override
    public List<Group> getGroupsByTeacher(Teacher teacher) {
        return getGroupsByOneArgIdSql(teacher, GET_GROUPS_LIST_BY_TEACHER_ID, TEACHER);
    }

    @Override
    public List<Group> getGroupsByStudent(Student student) {
        return getGroupsByOneArgIdSql(student, GET_GROUPS_LIST_BY_STUDENT_ID, STUDENT);
    }

    public List<Group> getGroupsByOneArgIdSql(AbstractEntity entity, String sql, String item) {
        ResultSet set = null;
        List<Group> groups = new ArrayList<>();

        if (entity == null) return groups;

        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, entity.getId());
            set = st.executeQuery();

            groups.addAll(extractObjects(set));
            log.debug("Получили из БД group list with {}Id={}", item, entity.getId());
        } catch (SQLException e) {
            log.error("Не удалось извлечь список groups по " + item + "Id=" + entity.getId(), e);
        } finally {
            closeResource(set);
        }
        return groups;
    }

    @Override
    public Group getGroupById(int id) {
        return get(id, GET_GROUP_BY_ID, GROUP, GROUP_NOT_EXISTS);
    }

    @Override
    public boolean putGroupIfNotExists(Group group) {
        if (group == null) return false;
        boolean result = false;
        Connection con = null;
        PreparedStatement st = null;
        ResultSet set = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(SET_GROUP_RETURNING_GROUP_ID);
            st.setString(1, group.getTitle());
            st.setString(2, group.getDescription());
            con.setAutoCommit(false);
            set = st.executeQuery();

            if (set.next()) {
                result = true;
                con.commit();
                log.debug("добавили БД group без teacher)");
            } else {
                log.debug("Не удалось добавить group без teacher в БД");
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию извлечения group с teacher (или без)", e);
        } finally {
            closeResources(set, st, con);
        }
        return result;
    }

    @Override
    public boolean changeGroupsParametersIfExists(Group newGr) {
        if (isNotExistsInDb(newGr)) {
            log.debug("Нельзя менять параметры обьекта, которого не существует в БД");
            return false;
        }
        Group oldGr = getGroupById(newGr.getId());
        if (isNotExistsInDb(oldGr)) {
            log.debug("Нельзя менять параметры обьекта, которого не существует в БД");
            return false;
        }
        if (oldGr.equals(newGr)) {//сравниваем параметры title description и teacherId
            log.debug("Не нужно менять параметры когда они и так одинаковые");
            return false;
        }
        log.debug("Что ж, придётся менять параметры наших объектов");
        log.debug("Старый объект = {}", oldGr);
        log.debug("Новый объект = {}", newGr);

        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(CHANGE_GROUP_BY_ID);
            st.setString(1, newGr.getTitle());
            if (newGr.getTeacher() == null) {
                st.setNull(2, Types.INTEGER);
            } else {
                st.setInt(2, newGr.getTeacher().getId());
            }
            st.setString(3, newGr.getDescription());
            st.setInt(4, newGr.getId());
            con.setAutoCommit(false);
            int num = st.executeUpdate();
            log.debug("st.executeUpdate() будет равен {}", num);
            if (num > 0) {
                log.info("Обновили group в БД по id={}", newGr.getId());
                con.commit();
                return true;
            } else {
                log.info("Не удалось обновить group в БД по id={}, такого id нет в базе", newGr.getId());
                con.rollback();
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию обновления group по id", e);
            rollback(con);
        } finally {
            closeResources(st, con);
        }
        return false;
    }

    private boolean isNotExistsInDb(AbstractEntity e) {
        return (e == null || e.getId() <= 0);
    }

    @Override
    public boolean addStudentInGroup(Group group, Student student) {
        return twoIntIdArgQuery(group, student, INCLUDE_STUDENT_IN_GROUP_BY_IDS, INCLUDE);
    }

    @Override
    public boolean removeStudentFromGroup(Group group, Student student) {
        return twoIntIdArgQuery(group, student, EXCLUDE_STUDENT_FROM_GROUP_BY_IDS, EXCLUDE);
    }

    @Override
    public boolean addSubjectInGroup(Group group, Subject subject) {
        return twoIntIdArgQuery(group, subject, INCLUDE_SUBJECT_IN_GROUP_BY_IDS, INCLUDE);
    }

    @Override
    public boolean removeSubjectFromGroup(Group group, Subject subject) {
        return twoIntIdArgQuery(group, subject, EXCLUDE_SUBJECT_FROM_GROUP_BY_IDS, EXCLUDE);
    }

    private boolean twoIntIdArgQuery(Group group, AbstractEntity entity, String sql, String action) {
        if (isAllExistInDB(group, entity)) {
            log.debug("Нельзя {} {} в group. Group или entity нет в БД (они null или их id<0)", action, entity);
            return false;
        }

        boolean result = false;
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(sql);
            st.setInt(1, group.getId());
            st.setInt(2, entity.getId());
            con.setAutoCommit(false);

            if (st.executeUpdate() == 1) {
                result = true;
                con.commit();
                log.debug("{} entity c id={} из group с id={}", action, entity.getId(), group.getId());
            } else {
                rollback(con);
                log.debug("Не удалось {} entity из id={} в group с id={}", action, entity.getId(), group.getId());
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось" + action + " entity c id=" + entity.getId() + " в group с id=" + group.getId(), e);
        } finally {
            closeResources(st, con);
        }
        return result;
    }

    private boolean isAllExistInDB(AbstractEntity e1, AbstractEntity e2) {
        return (e1 == null || e2 == null || e1.getId() <= 0 || e2.getId() <= 0);
    }

    @Override
    public boolean deleteGroupIfExists(int groupId) {
        boolean result = false;
        Connection con = null;
        PreparedStatement firstPs = null;
        PreparedStatement secondPs = null;
        PreparedStatement thirdPs = null;
        try {
            con = pool.getConnection();
            firstPs = con.prepareStatement(DELETE_GROUP_BY_ID);
            firstPs.setInt(1, groupId);
            con.setAutoCommit(false);

            if (firstPs.executeUpdate() > 0) {
                log.debug("удалили {} из БД по id={})", GROUP, groupId);
                secondPs = execOneArgIntSql(con, groupId, EXCLUDE_ALL_STUDENTS_FROM_GROUP_BY_ID, STUDENT);
                thirdPs = execOneArgIntSql(con, groupId, EXCLUDE_ALL_SUBJECTS_FROM_GROUP_BY_ID, SUBJECT);
                result = true;
                con.commit();
            } else {
                con.rollback();
                log.debug("Не удалось удалить {} из БД по id={}", GROUP, groupId);
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию удаления {} из БД по id", GROUP, e);
        } finally {
            closeResources(firstPs, secondPs, thirdPs, con);
        }
        return result;
    }

    private PreparedStatement execOneArgIntSql(Connection c, int id, String sql, String item) throws SQLException {
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setInt(1, id);
        int num = ps.executeUpdate();
        log.debug("Из группы с id={} перед её удалением отвязали {} {}", id, num, item);
        return ps;
    }
}