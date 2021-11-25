package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class AbstractPostgresRepo<T extends AbstractEntity> {
    protected volatile ComboPooledDataSource pool;

    public AbstractPostgresRepo(ComboPooledDataSource pool) {
        this.pool = pool;
    }

    protected void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                log.error("Возникла проблема при закрытии ресурса", e);
            }
        }
    }

    protected void closeResources(AutoCloseable... resources) {
        for (AutoCloseable res : resources) {
            closeResource(res);
        }
    }

    protected void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                log.error("Не удалось откатить изменения", ex);
            }
        }
    }

    protected boolean delete(int id, String sql, String item) {
        boolean result = false;
        Connection con = null;
        PreparedStatement st = null;
        try {
            con = pool.getConnection();
            st = con.prepareStatement(sql);
            st.setInt(1, id);
            con.setAutoCommit(false);

            if (st.executeUpdate() > 0) {
                result = true;
                con.commit();
                log.debug("удалили {} из БД по по id)", item);
            } else {
                con.rollback();
                log.debug("Не удалось удалить {} из БД по по id", item);
            }
        } catch (SQLException e) {
            rollback(con);
            log.error("Не удалось совершить операцию удаления {} из БД по id", item, e);
        } finally {
            closeResources(st, con);
        }
        return result;
    }

    protected T get(int id, String sql, String item, T notExists) {
        T obj = notExists;
        ResultSet rs = null;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);

            rs = st.executeQuery();
            if (rs.next()) {
                obj = extractObject(rs);
                log.info("Извлекли {} из БД по id={}", item, id);
            } else {
                log.info("Не удалось извлечь " + item + " из БД по id={}, такого id нет в базе", id);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения " + item + "s set по id=" + id, e);
        } finally {
            closeResource(rs);
        }
        return obj;
    }

    protected Set<T> getAllByOneIntArg(int id, String sql, String item) {
        Set<T> objects = new HashSet<>();
        ResultSet set = null;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);

            set = st.executeQuery();
            while (set.next()) {
                objects = extractObjects(set);
                log.info("Извлекли {}s Set из БД по id={}", item, id);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения " + item + "s set по id=" + id, e);
        } finally {
            closeResource(set);
        }
        return objects;
    }

    protected Set<T> extractObjects(ResultSet set) throws SQLException {
        Set<T> o = new HashSet<>();
        while (set.next()) {
            o.add(extractObject(set));
        }
        return o;
    }

    protected abstract T extractObject(ResultSet set) throws SQLException;
}