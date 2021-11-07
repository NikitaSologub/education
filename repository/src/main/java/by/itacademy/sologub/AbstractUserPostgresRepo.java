package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.ID_NOT_EXISTS;
import static by.itacademy.sologub.constants.SqlQuery.DELETE_CREDENTIAL_BY_ID;
import static by.itacademy.sologub.constants.SqlQuery.SET_CREDENTIAL_AND_GET_ID;
import static by.itacademy.sologub.constants.SqlQuery.UPDATE_CREDENTIAL_BY_ID;

@Slf4j
public abstract class AbstractUserPostgresRepo extends AbstractPostgresRepo {

    public AbstractUserPostgresRepo(ComboPooledDataSource pool) {
        super(pool);
    }

    public boolean putUserIfNotExists(User user, String sql) {
        String entity = user.getClass().getSimpleName();
        Connection con = null;
        PreparedStatement firstSt = null;
        PreparedStatement secondSt = null;

        try {
            con = pool.getConnection();
            con.setAutoCommit(false);
            firstSt = con.prepareStatement(SET_CREDENTIAL_AND_GET_ID);
            int id = setCredAndGetId(user, firstSt);

            if (id != ID_NOT_EXISTS) {
                secondSt = con.prepareStatement(sql);

                if (isInsert(id, user, secondSt)) {
                    log.info(entity + " {} добавлен бд", user);
                    con.commit();
                    return true;
                } else {
                    rollback(con);
                    log.info("Не удалось совершить операцию добавления нового " + entity, user);
                }
            } else {
                log.info("Нельзя добавить " + entity + ", такой логин уже существует");
                con.rollback();
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию добавления нового " + entity, e);
        } finally {
            closeResources(secondSt, firstSt, con);
        }
        return false;
    }

    private int setCredAndGetId(User user, PreparedStatement st) throws SQLException {
        st.setString(1, user.getCredential().getLogin());
        st.setString(2, user.getCredential().getPassword());
        ResultSet set = st.executeQuery();
        if (set.next()) {
            return set.getInt(ID);
        }
        return ID_NOT_EXISTS;
    }

    private boolean isInsert(int id, User user, PreparedStatement st) throws SQLException {
        st.setString(1, user.getFirstname());
        st.setString(2, user.getLastname());
        st.setString(3, user.getPatronymic());
        st.setDate(4, Date.valueOf(user.getDateOfBirth()));
        st.setInt(5, id);
        return st.executeUpdate() > 0;
    }

    public boolean changeUsersParametersIfExists(User user, String sql) {
        String entity = user.getClass().getSimpleName();
        Connection con = null;
        PreparedStatement firstSt = null;
        PreparedStatement secondSt = null;
        try {
            con = pool.getConnection();
            firstSt = con.prepareStatement(sql);
            firstSt.setString(1, user.getFirstname());
            firstSt.setString(2, user.getLastname());
            firstSt.setString(3, user.getPatronymic());
            firstSt.setDate(4, Date.valueOf(user.getDateOfBirth()));
            firstSt.setInt(5, user.getCredential().getId());
            secondSt = con.prepareStatement(UPDATE_CREDENTIAL_BY_ID);
            secondSt.setString(1, user.getCredential().getLogin());
            secondSt.setString(2, user.getCredential().getPassword());
            secondSt.setInt(3, user.getCredential().getId());
            con.setAutoCommit(false);

            if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                con.commit();
                log.info(entity + " {} изменили в БД.", user);
                return true;
            } else {
                con.rollback();
                log.info("не удалось изменить " + entity + " {} в БД.", user);
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию изменения " + entity, e);
        } finally {
            closeResources(secondSt, firstSt, con);
        }
        return false;
    }

    public boolean deleteUser(User user, String sql, User notExist) {
        String entity = user.getClass().getSimpleName();
        Connection con = null;
        PreparedStatement firstSt = null;
        PreparedStatement secondSt = null;

        if (user != notExist) {
            try {
                con = pool.getConnection();
                firstSt = con.prepareStatement(sql);
                firstSt.setInt(1, user.getCredential().getId());
                secondSt = con.prepareStatement(DELETE_CREDENTIAL_BY_ID);
                secondSt.setInt(1, user.getCredential().getId());
                con.setAutoCommit(false);

                if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                    con.commit();
                    log.info(entity + " {} удалили из БД.", user);
                    return true;
                } else {
                    log.info("Не удалось удалить " + entity + " {} из БД.", user);
                    con.rollback();
                }
            } catch (SQLException e) {
                rollback(con);
                log.error("Не удалось совершить операцию удаления " + entity, e);
            } finally {
                closeResources(secondSt, firstSt, con);
            }
        } else {
            log.info("Нельзя удалить" + entity + " {}. Логина не существует в базе", user);
        }
        return false;
    }
}