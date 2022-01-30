package by.itacademy.sologub.postgres;

import by.itacademy.sologub.model.User;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.ID_NOT_EXISTS;
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_CREDENTIAL_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_USER_BY_CREDENTIAL_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_USERS_LIST;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_USER_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_USER_BY_LOGIN;
import static by.itacademy.sologub.postgres.queries.SqlQuery.INSERT_USER;
import static by.itacademy.sologub.postgres.queries.SqlQuery.SET_CREDENTIAL_AND_GET_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.UPDATE_CREDENTIAL_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.UPDATE_USER_BY_CREDENTIAL_ID;

@Slf4j
public abstract class AbstractUserPostgresRepo<T extends User> extends AbstractPostgresRepo<T> {

    public AbstractUserPostgresRepo(ComboPooledDataSource pool) {
        super(pool);
    }

    protected abstract String getRole();

    protected abstract T getNotExists();

    protected abstract T getPasswordWrong();

    public Set<T> getUsersSet() {
        Set<T> users = new HashSet<>();
        ResultSet rs = null;

        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_USERS_LIST)) {
            ps.setString(1, getRole());
            rs = ps.executeQuery();

            users = extractUsers(rs);
        } catch (SQLException e) {
            log.error("Не удалось извлечь set учителей из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return users;
    }

    private Set<T> extractUsers(ResultSet rs) throws SQLException {
        Set<T> users = new HashSet<>();
        log.debug("Создали пустой set и переходим к извлечению данных");
        while (rs.next()) {
            T u = extractObject(rs);
            log.debug("Извлекли обьект {} {} добавляем его в set", getRole(), u);
            users.add(u);
        }
        log.debug("Возвращаем set обьектов {}", getRole());
        return users;
    }

    protected T getUserIfExistsOrGetSpecialValue(String login) {
        ResultSet rs = null;
        T user = getNotExists();

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_USER_BY_LOGIN)) {
            ps.setString(1, login);
            ps.setString(2, getRole());
            rs = ps.executeQuery();
            if (rs.next()) {
                user = extractObject(rs);
            }
        } catch (SQLException e) {
            log.error("Не удалось извлечь " + getRole() + " из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return user;
    }

    protected T getUserIfExistsOrGetSpecialValue(int id) {
        ResultSet rs = null;
        T user = getNotExists();

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_USER_BY_ID)) {
            ps.setInt(1, id);
            ps.setString(2, getRole());
            rs = ps.executeQuery();
            if (rs.next()) {
                user = extractObject(rs);
            }
        } catch (SQLException e) {
            log.error("Не удалось извлечь " + getRole() + " из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return user;
    }

    protected T getUserIfExistsOrGetSpecialValue(String login, String password) {
        T user = getUserIfExistsOrGetSpecialValue(login);

        if (getNotExists() != user) {
            String dbPassword = user.getCredential().getPassword();

            if (password != null && password.equals(dbPassword)) {
                log.debug("Возвращаем {} {} пароль={} верный", getRole(), login, password);

            } else {
                user = getPasswordWrong();
                log.debug("Не удалось взять {} {} пароль={} не верный", getRole(), login, password);
            }
        }
        return user;
    }

    protected boolean putUserIfNotExists(T user) {
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
                secondSt = con.prepareStatement(INSERT_USER);

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
                rollback(con);
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию добавления нового " + entity, e);
        } finally {
            closeResources(secondSt, firstSt, con);
        }
        return false;
    }

    private int setCredAndGetId(T user, PreparedStatement st) throws SQLException {
        st.setString(1, user.getCredential().getLogin());
        st.setString(2, user.getCredential().getPassword());
        ResultSet set = st.executeQuery();
        if (set.next()) {
            return set.getInt(ID);
        }
        return ID_NOT_EXISTS;
    }

    private boolean isInsert(int id, T user, PreparedStatement st) throws SQLException {
        st.setString(1, user.getFirstname());
        st.setString(2, user.getLastname());
        st.setString(3, user.getPatronymic());
        st.setDate(4, Date.valueOf(user.getDateOfBirth()));
        st.setInt(5, id);
        st.setString(6, getRole());
        return st.executeUpdate() > 0;
    }

    protected boolean changeUsersParametersIfExists(T user) {
        String entity = user.getClass().getSimpleName();
        Connection con = null;
        PreparedStatement firstSt = null;
        PreparedStatement secondSt = null;
        try {
            con = pool.getConnection();
            firstSt = con.prepareStatement(UPDATE_USER_BY_CREDENTIAL_ID);
            firstSt.setString(1, user.getFirstname());
            firstSt.setString(2, user.getLastname());
            firstSt.setString(3, user.getPatronymic());
            firstSt.setDate(4, Date.valueOf(user.getDateOfBirth()));
            firstSt.setInt(5, user.getCredential().getId());
            firstSt.setString(6, getRole());
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
                rollback(con);
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

    protected boolean deleteUser(T user) {
        String entity = user.getClass().getSimpleName();
        Connection con = null;
        PreparedStatement firstSt = null;
        PreparedStatement secondSt = null;

        if (user != getNotExists()) {
            try {
                con = pool.getConnection();
                firstSt = con.prepareStatement(DELETE_USER_BY_CREDENTIAL_ID);
                firstSt.setString(1, getRole());
                firstSt.setInt(2, user.getCredential().getId());
                secondSt = con.prepareStatement(DELETE_CREDENTIAL_BY_ID);
                secondSt.setInt(1, user.getCredential().getId());
                con.setAutoCommit(false);

                if (firstSt.executeUpdate() > 0 && secondSt.executeUpdate() > 0) {
                    con.commit();
                    log.info(entity + " {} удалили из БД.", user);
                    return true;
                } else {
                    log.info("Не удалось удалить " + entity + " {} из БД.", user);
                    rollback(con);
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