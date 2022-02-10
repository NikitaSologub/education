package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.AdminRepo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;

@Slf4j
@Repository
public class AdminRepoHibernateImpl extends AbstractCrudRepoJpa<Admin> implements AdminRepo {
    @Autowired
    public AdminRepoHibernateImpl(SessionFactory sf) {
        super(sf, Admin.class);
    }

    @Override
    public Set<Admin> getAdminsList() {
        return new HashSet<>(getAll());
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        Admin a = getByNamedQueryStringArgument("getAdminByLogin", login, LOGIN);
        if (a == null) {
            log.debug("Не получилось получить обьект Admin по {}={}", LOGIN, login);
            return ADMIN_NOT_EXISTS;
        }
        return a;
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        Admin a = getAdminIfExistsOrGetSpecialValue(login);
        if (ADMIN_NOT_EXISTS != a) {
            if (a.getCredential().getPassword().equals(password)) {
                return a;
            } else {
                log.debug("Не получилось получить обьект Admin по {}={}. Пароль не верен", LOGIN, login);
                return ADMIN_PASSWORD_WRONG;
            }
        }
        return a;
    }

    @Override
    public boolean putAdminIfNotExists(Admin admin) {
        return input(admin);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return change(newAdmin);
    }

    @Override
    public boolean deleteAdmin(String login) {
        Admin a = getAdminIfExistsOrGetSpecialValue(login);
        if (ADMIN_NOT_EXISTS == a) {
            log.debug("Не получилось удалить обьект Admin по {}={}", LOGIN, login);
            return false;
        }
        return deleteAdmin(a);
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        return remove(admin);
    }

    @Override
    protected Admin getEmptyObj() {
        return ADMIN_NOT_EXISTS;
    }
}