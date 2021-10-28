package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Slf4j
public abstract class AbstractPostgresRepo {
    protected ComboPooledDataSource pool;

    public AbstractPostgresRepo(ComboPooledDataSource pool) {
        this.pool = pool;
    }

    protected void closeConnectionAndResources(Connection con, PreparedStatement ps, ResultSet rs) {
        closeResource(rs);
        closeConnectionAndResources(con, ps);
    }

    protected void closeConnectionAndResources(Connection con, PreparedStatement ps) {
        closeResource(ps);
        closeResource(con);
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
}