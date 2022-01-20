package by.itacademy.sologub.postgres;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.CredentialRepo;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.ID_NOT_EXISTS;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.ConstantObject.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_CREDENTIAL_BY_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.DELETE_CREDENTIAL_BY_LOGIN;
import static by.itacademy.sologub.postgres.queries.SqlQuery.GET_CREDENTIAL_BY_LOGIN;
import static by.itacademy.sologub.postgres.queries.SqlQuery.SET_CREDENTIAL_AND_GET_ID;
import static by.itacademy.sologub.postgres.queries.SqlQuery.SET_CREDENTIAL_IF_NOT_EXISTS;
import static by.itacademy.sologub.postgres.queries.SqlQuery.UPDATE_CREDENTIAL;

@Slf4j
@Repository
public class CredentialRepoPostgresImpl extends AbstractPostgresRepo<Credential> implements CredentialRepo {
    @Autowired
    public CredentialRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    @Override
    protected Credential extractObject(ResultSet rs) throws SQLException {
        return new Credential()
                .withId(rs.getInt(ID))
                .withLogin(rs.getString(LOGIN))
                .withPassword(rs.getString(PASSWORD));
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        Credential cred = getCredentialIfExistsOrGetSpecialValue(login);

        if (cred != null && LOGIN_NOT_EXISTS != cred) {
            boolean isPasswordRight = cred.getPassword().equals(password);
            if (isPasswordRight) {
                log.debug("Учётные данные по логину={} и паролю={} есть", login, password);
                return cred;
            } else {
                log.debug("Учётные данные по логину={} есть но пароль={} неправильный", login, password);
                return PASSWORD_WRONG;
            }
        }
        log.debug("Учётных данных по логину={} и паролю={} нет в БД", login, password);
        return LOGIN_NOT_EXISTS;
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        Credential cred = LOGIN_NOT_EXISTS;
        ResultSet rs = null;

        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(GET_CREDENTIAL_BY_LOGIN)) {
            st.setString(1, login);
            rs = st.executeQuery();

            if (rs.next()) {
                cred = extractObject(rs);
                log.debug("Получили из БД {}", cred);
            } else {
                log.debug("Логин {} не существует", login);
            }
        } catch (SQLException e) {
            log.error("Не удалось достать Credential по логину", e);
        } finally {
            closeResource(rs);
        }
        return cred;
    }

    @Override
    public boolean putCredentialIfNotExists(String login, String password) {
        boolean result = false;

        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(SET_CREDENTIAL_IF_NOT_EXISTS)) {
            st.setString(1, login);
            st.setString(2, password);

            if (st.executeUpdate() > 0) {
                log.info("Учётная запись логин={} пароль={} добавлена в бд", login, password);
                result = true;
            } else {
                log.info("Учётная запись логин={} пароль={} не добавлена в бд. Такой логин уже существует", login, password);
            }

        } catch (SQLException e) {
            log.error("Не удалось совершить операцию добавления учётной записи", e);
        }
        return result;
    }

    @Override
    public int putCredentialIfNotExistsAndGetId(Credential credential) {
        ResultSet rs = null;
        int id = ID_NOT_EXISTS;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(SET_CREDENTIAL_AND_GET_ID)) {
            st.setString(1, credential.getLogin());
            st.setString(2, credential.getPassword());
            rs = st.executeQuery();

            if (rs.next()) {
                id = rs.getInt(ID);
                log.info("Учётная запись id={} логин={} пароль={} добавлена в бд", id, credential.getLogin(), credential.getPassword());
            } else {
                log.info("Учётная запись логин={} не добавлена в бд , логин уже существует", credential.getLogin());
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию добавления учётной записи", e);
        } finally {
            closeResource(rs);
        }
        return id;
    }

    @Override
    public boolean changeCredentialIfExists(String login, String password) {
        boolean result = false;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(UPDATE_CREDENTIAL)) {
            st.setString(1, password);
            st.setString(2, login);

            if (st.executeUpdate() > 0) {
                log.info("Учётная запись логин={} пароль={} изменена в бд", login, password);
                result = true;
            } else {
                log.info("Учётная запись логин={} пароль={} не изменена в бд. Такого логина не существует", login, password);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию изменения учётной записи", e);
        }
        return result;
    }

    @Override
    public boolean deleteCredentialIfExists(String login) {
        boolean result = false;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(DELETE_CREDENTIAL_BY_LOGIN)) {
            st.setString(1, login);

            if (st.executeUpdate() > 0) {
                log.info("Учётная запись логин={} удалена из бд", login);
                result = true;
            } else {
                log.info("Учётная запись логин={} не удалена из бд. Такого логина не существует", login);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию удаления пользователя", e);
        }
        return result;
    }

    @Override
    public boolean deleteCredentialIfExists(int id) {
        boolean result = false;
        try (Connection con = pool.getConnection(); PreparedStatement st = con.prepareStatement(DELETE_CREDENTIAL_BY_ID)) {
            st.setInt(1, id);

            if (st.executeUpdate() > 0) {
                log.info("Учётная запись id={} удалена из бд", id);
                result = true;
            } else {
                log.info("Учётная запись id={} не удалена из бд. Такого логина не существует", id);
            }
        } catch (SQLException e) {
            log.error("Не удалось совершить операцию удаления пользователя", e);
        }
        return result;
    }
}