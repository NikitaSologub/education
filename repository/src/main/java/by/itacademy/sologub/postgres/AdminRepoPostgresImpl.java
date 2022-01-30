package by.itacademy.sologub.postgres;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.role.Role;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

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

@Slf4j
@Repository
public class AdminRepoPostgresImpl extends AbstractUserPostgresRepo<Admin> implements AdminRepo {
    @Autowired
    public AdminRepoPostgresImpl(ComboPooledDataSource pool) {
        super(pool);
    }

    @Override //todo - вынести метод ниже
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
    public Set<Admin> getAdminsList() {
        return getUsersSet();
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        return getUserIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        return getUserIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putAdminIfNotExists(Admin a) {
        return putUserIfNotExists(a);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return changeUsersParametersIfExists(newAdmin);
    }

    @Override
    public boolean deleteAdmin(String login) {
        Admin a = getAdminIfExistsOrGetSpecialValue(login);
        return deleteAdmin(a);
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        return deleteUser(admin);
    }

    @Override
    protected String getRole() {
        return String.valueOf(Role.ADMIN);
    }

    @Override
    protected Admin getNotExists() {
        return ADMIN_NOT_EXISTS;
    }

    @Override
    protected Admin getPasswordWrong() {
        return ADMIN_PASSWORD_WRONG;
    }
}