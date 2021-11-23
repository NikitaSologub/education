package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        PreparedStatement st;
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
                log.info("Извлекли " + item + " из БД по id={}", id);
            } else {
                log.info("Не удалось извлечь " + item + " из БД по id={}, такого id нет в базе", id);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию извлечения " + item + " по id=" + id, e);
        } finally {
            closeResource(rs);
        }
        return obj;
    }

    protected abstract T extractObject(ResultSet set) throws SQLException;
}