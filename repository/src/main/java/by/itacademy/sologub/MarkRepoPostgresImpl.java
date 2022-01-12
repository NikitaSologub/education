package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.MARK;
import static by.itacademy.sologub.constants.Attributes.POINT;
import static by.itacademy.sologub.constants.Attributes.SUBJECT_ID_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.SqlQuery.DELETE_MARK;
import static by.itacademy.sologub.constants.SqlQuery.GET_MARKS_BY_STUDENT_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_MARKS_BY_STUDENT_ID_AND_SUBJECT_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_MARKS_BY_SUBJECT_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_MARK_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.SET_MARK;
import static by.itacademy.sologub.constants.SqlQuery.UPDATE_MARK;

@Slf4j
@Repository
public class MarkRepoPostgresImpl extends AbstractPostgresRepo<Mark> implements MarkRepo {
    @Autowired
    public MarkRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    @Override
    protected Mark extractObject(ResultSet set) throws SQLException {
        return new Mark()
                .withId(set.getInt(ID))
                .withSubject(new Subject()
                        .withId(set.getInt(SUBJECT_ID_DB_FIELD))
                        .withTitle(set.getString(TITLE)))
                .withDate(set.getDate(DATE).toLocalDate())
                .withPoint(set.getInt(POINT));
    }

    @Override
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject s, int studentId) {
        Set<Mark> marks = new HashSet<>();
        ResultSet set = null;
        try (Connection con = pool.getConnection();
             PreparedStatement st = con.prepareStatement(GET_MARKS_BY_STUDENT_ID_AND_SUBJECT_ID)) {
            st.setInt(1, studentId);
            st.setInt(2, s.getId());

            set = st.executeQuery();
            while (set.next()) {
                marks = extractObjects(set);
                log.info("Извлекли {}s Set из БД по student_id={} и subject_id={}", MARK, studentId, s.getId());
            }
        } catch (SQLException e) {
            log.error("Ошибка извлечегия " + MARK + "s set по student_id=" + studentId + " и subject_id=" + s.getId(), e);
        } finally {
            closeResource(set);
        }
        return marks;
    }

    @Override
    public Set<Mark> getAllMarksBySubject(Subject s) {
        return getAllByOneIntArg(s.getId(), GET_MARKS_BY_SUBJECT_ID, MARK);
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int id) {
        return getAllByOneIntArg(id, GET_MARKS_BY_STUDENT_ID, MARK);
    }

    @Override
    public Mark getMark(int id) {
        return get(id, GET_MARK_BY_ID, MARK, MARK_NOT_EXISTS);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        if (checkForInvalid(mark, studentId)) return false;
        log.debug("оценка {} и studentId={} прошли валидацию", mark, studentId);
        Connection con = null;
        PreparedStatement st = null;

        try {
            con = pool.getConnection();
            st = con.prepareStatement(SET_MARK);
            st.setInt(1, studentId);
            st.setInt(2, mark.getSubject().getId());
            st.setDate(3, Date.valueOf(mark.getDate()));
            st.setInt(4, mark.getPoint());
            con.setAutoCommit(false);

            if (st.executeUpdate() > 0) {
                log.debug("добавили {} студенту с id={}", mark, studentId);
                con.commit();
                return true;
            } else {
                log.debug("Не удалось добавить {} студенту с id={}", mark, studentId);
                rollback(con);
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось добавить mark. Предмета или студента по таким id не существует в бд)", e);
        } finally {
            closeResources(st, con);
        }
        return false;
    }

    private boolean checkForInvalid(Mark mark, int studentId) {
        return (mark == null || mark.getSubject() == null || studentId <= 0 || mark.getSubject().getId() <= 0);
    }

    @Override
    public boolean changeMark(Mark newMark) {
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(UPDATE_MARK);
            st.setDate(1, Date.valueOf(newMark.getDate()));
            st.setInt(2, newMark.getPoint());
            st.setInt(3, newMark.getId());
            st.setInt(4, newMark.getSubject().getId());
            con.setAutoCommit(false);
            int num = st.executeUpdate();
            log.debug("st.executeUpdate() будет равен {}", num);
            if (num > 0) {
                log.info("Обновили group в БД по id={}", newMark.getId());
                con.commit();
                return true;
            } else {
                log.info("Не удалось обновить group в БД по id={}, такого id нет в базе", newMark.getId());
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

    @Override
    public boolean deleteMark(int id) {
        return delete(id, DELETE_MARK, MARK);
    }
}