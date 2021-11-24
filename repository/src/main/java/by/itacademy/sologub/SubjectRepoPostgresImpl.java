package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.SUBJECT;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;
import static by.itacademy.sologub.constants.SqlQuery.DELETE_SUBJECT_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.EXCLUDE_SUBJECT_FROM_ALL_GROUPS_BY_SUBJECT_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_SUBJECTS_LIST;
import static by.itacademy.sologub.constants.SqlQuery.GET_SUBJECTS_LIST_BY_GROUP_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_SUBJECT_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.SET_SUBJECT;
import static by.itacademy.sologub.constants.SqlQuery.UPDATE_SUBJECT_BY_ID;

@Slf4j
public class SubjectRepoPostgresImpl extends AbstractPostgresRepo<Subject> implements SubjectRepo {
    private static volatile SubjectRepoPostgresImpl instance;

    private SubjectRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    public static SubjectRepoPostgresImpl getInstance(ComboPooledDataSource pool) {
        if (instance == null) {
            synchronized (SubjectRepoPostgresImpl.class) {
                if (instance == null) {
                    instance = new SubjectRepoPostgresImpl(pool);
                }
            }
        }
        return instance;
    }

    private List<Subject> extractObjects(ResultSet set) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        while (set.next()) {
            subjects.add(extractObject(set));
        }
        return subjects;
    }

    @Override
    protected Subject extractObject(ResultSet rs) throws SQLException {
        return new Subject()
                .withId(rs.getInt(ID))
                .withTitle(rs.getString(TITLE));
    }

    @Override
    public List<Subject> getSubjectsList() {
        List<Subject> subjects = new ArrayList<>();
        ResultSet rs = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_SUBJECTS_LIST)) {
            rs = st.executeQuery();
            subjects = extractObjects(rs);
            log.info("Извлекли все Subjects из БД");
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения Subject list", e);
        } finally {
            closeResource(rs);
        }
        return subjects;
    }

    @Override
    public Set<Subject> getSubjectsByGroupId(int groupId) {
        List<Subject> subjects = new ArrayList<>();
        ResultSet set = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_SUBJECTS_LIST_BY_GROUP_ID)) {
            st.setInt(1, groupId);
            set = st.executeQuery();

            subjects = extractObjects(set);
            log.info("Извлекли все Subjects из БД по groupId={}", groupId);
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения Subject set по groupId=" + groupId, e);
        } finally {
            closeResource(set);
        }
        return new HashSet<>(subjects);
    }

    @Override
    public Subject getSubjectIfExistsOrGetSpecialValue(int id) {
        return get(id, GET_SUBJECT_BY_ID, SUBJECT, SUBJECT_NOT_EXISTS);
    }

    @Override
    public boolean putSubjectIfNotExists(Subject subject) {
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(SET_SUBJECT)) {
            st.setString(1, subject.getTitle());

            if (st.executeUpdate() > 0) {
                log.info("Добавили Subject={} в БД", subject.getTitle());
                return true;
            } else {
                log.info("Не удалось добавить Subject={} в БД. Title не уникален", subject.getTitle());
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию добавления Subject", e);
        }
        return false;
    }

    @Override
    public boolean changeSubjectsParametersIfExists(Subject subject) {
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(UPDATE_SUBJECT_BY_ID)) {
            st.setString(1, subject.getTitle());
            st.setInt(2, subject.getId());

            if (st.executeUpdate() > 0) {
                log.info("Обновили Subject в БД по id={}", subject.getId());
                return true;
            } else {
                log.info("Не удалось обновить Subject в БД по id={}, такого id нет в базе", subject.getId());
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию обновления Subject по id", e);
        }
        return false;
    }

    @Override
    public boolean deleteSubject(Subject subject) {
        int id = subject.getId();
        if (delete(subject.getId(), DELETE_SUBJECT_BY_ID, SUBJECT)) {
            Connection con = null;
            PreparedStatement st;
            try {
                con = pool.getConnection();
                st = con.prepareStatement(EXCLUDE_SUBJECT_FROM_ALL_GROUPS_BY_SUBJECT_ID);
                st.setInt(1, id);

                int num = st.executeUpdate();
                log.debug("исключили предмет по subject_id={} из {} групп)", id, num);
            } catch (SQLException e) {
                rollback(con);
                log.error("Не удалось исключитьпредмет из групп по его id={}", id, e);
            }
            return true;
        }
        return false;
    }
}