package by.itacademy.sologub.memory;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.AdminRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ADMIN;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;

@Slf4j
@Repository
public class AdminRepoMemoryImpl extends AbstractUserMemoryRepo<Admin> implements AdminRepo {
    static int CURRENT_MAX_ADMIN_ID = 139051;

    @Autowired
    public AdminRepoMemoryImpl(CredentialRepoMemoryImpl credentialRepo) {
        super(credentialRepo);
    }

    @Override
    protected int getCurrentMaxIdAndIncrement() {
        return CURRENT_MAX_ADMIN_ID++;
    }

    @Override
    protected Admin getNotExists() {
        return ADMIN_NOT_EXISTS;
    }

    @Override
    protected Admin getPasswordWrong() {
        return ADMIN_PASSWORD_WRONG;
    }

    @Override
    protected String getType() {
        return ADMIN;
    }

    @Override
    public Set<Admin> getAdminsList() {
        return getUserSet();
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
    public boolean putAdminIfNotExists(Admin admin) {
        return putUserIfNotExists(admin);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newA) {
        return changeUserParametersIfExists(newA);
    }

    @Override
    public boolean deleteAdmin(String login) {
        return deleteUser(login);
    }

    @Override
    public boolean deleteAdmin(Admin Admin) {
        return deleteAdmin(Admin.getCredential().getLogin());
    }
}