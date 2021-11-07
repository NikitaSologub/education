package by.itacademy.sologub;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.CREDENTIAL_ID_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.DATE_OF_BIRTH_DB_FIELD;
import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;
import static by.itacademy.sologub.constants.SqlQuery.DELETE_ADMIN_BY_CREDENTIAL_ID;
import static by.itacademy.sologub.constants.SqlQuery.GET_ADMINS_LIST;
import static by.itacademy.sologub.constants.SqlQuery.GET_ADMIN_BY_LOGIN;
import static by.itacademy.sologub.constants.SqlQuery.INSERT_ADMIN;
import static by.itacademy.sologub.constants.SqlQuery.UPDATE_ADMIN_BY_CREDENTIAL_ID;

@Slf4j
public class AdminRepoPostgresImpl extends AbstractUserPostgresRepo implements AdminRepo {
    private static volatile AdminRepoPostgresImpl adminRepo;
    private static volatile CredentialRepoPostgresImpl credentialRepo;

    private AdminRepoPostgresImpl(ComboPooledDataSource pool, CredentialRepoPostgresImpl repo) {
        super(pool);
        credentialRepo = repo;
    }

    public static AdminRepoPostgresImpl getInstance(ComboPooledDataSource pool, CredentialRepoPostgresImpl credRepo) {
        if (adminRepo == null) {
            synchronized (CredentialRepoHardcodeImpl.class) {
                if (adminRepo == null) {
                    adminRepo = new AdminRepoPostgresImpl(pool, credRepo);
                }
            }
        }
        return adminRepo;
    }

    @Override
    protected Admin extractObject(ResultSet rs) throws SQLException {
        return new Admin()
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
    public List<Admin> getAdminsList() {
        List<Admin> admins = new ArrayList<>();

        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement(GET_ADMINS_LIST);
             ResultSet rs = ps.executeQuery()) {

            admins = extractAdmins(rs);
        } catch (SQLException e) {
            log.error("Не удалось извлечь список админов из базы данных", e);
        }
        return admins;
    }

    private List<Admin> extractAdmins(ResultSet rs) throws SQLException {
        List<Admin> admins = new ArrayList<>();
        log.debug("Создали пустой лист и переходим к извлечению данных");
        while (rs.next()) {
            Admin s = extractObject(rs);
            log.debug("Извлекли обьект админа {} добавляем его в список", s);
            admins.add(s);
        }
        log.debug("Возвращаем список админов");
        return admins;
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        ResultSet rs = null;
        Admin admin = ADMIN_NOT_EXISTS;

        try (Connection con = pool.getConnection(); PreparedStatement ps = con.prepareStatement(GET_ADMIN_BY_LOGIN)) {
            ps.setString(1, login);
            rs = ps.executeQuery();
            if (rs.next()) {
                admin = extractObject(rs);
            }
        } catch (SQLException e) {
            log.error("Не удалось извлечь админа из базы данных", e);
        } finally {
            closeResource(rs);
        }
        return admin;
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        Admin admin = getAdminIfExistsOrGetSpecialValue(login);

        if (ADMIN_NOT_EXISTS != admin) {
            String dbPassword = admin.getCredential().getPassword();

            if (password != null && password.equals(dbPassword)) {
                log.debug("Возвращаем админа {} пароль={} верный", login, password);

            } else {
                admin = ADMIN_PASSWORD_WRONG;
                log.debug("Не удалось взять админа {} пароль={} не верный", login, password);
            }
        }
        return admin;
    }

    @Override
    public boolean putAdminIfNotExists(Admin a) {
        return putUserIfNotExists(a, INSERT_ADMIN);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return changeUsersParametersIfExists(newAdmin, UPDATE_ADMIN_BY_CREDENTIAL_ID);
    }

    @Override
    public boolean deleteAdmin(String login) {
        Admin a = adminRepo.getAdminIfExistsOrGetSpecialValue(login);
        return deleteAdmin(a);
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        return deleteUser(admin, DELETE_ADMIN_BY_CREDENTIAL_ID, ADMIN_NOT_EXISTS);
    }
}