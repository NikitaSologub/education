package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;

@Slf4j
@Repository
public class AdminRepoSpringOrmImpl extends AbstractSpringOrm<Admin> implements AdminRepo {
    @Autowired
    public AdminRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Admin.class, ADMIN_NOT_EXISTS);
    }

    @Override
    @JpaTransaction
    public Set<Admin> getAdminsList() {
        List<Admin> admins = findAll();
        log.info("получили список админов {} из БД", admins);
        return new HashSet<>();
    }

    @Override
    @JpaTransaction
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        Admin admin = getByNamedQueryStringArgument("getAdminByLogin", login, LOGIN);
        if (ADMIN_NOT_EXISTS == admin) {
            log.debug("Не получилось получить обьект {} по {}={}", type, LOGIN, login);
            return ADMIN_NOT_EXISTS;
        }
        return admin;
    }

    @Override
    @JpaTransaction
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        Admin admin = getByNamedQueryStringArgument("getAdminByLogin", login, LOGIN);
        if (ADMIN_NOT_EXISTS != admin) {
            if (admin.getCredential().getPassword().equals(password)) {
                return admin;
            } else {
                log.debug("Не получилось получить обьект Admin по {}={}. Пароль не верен", LOGIN, login);
                return ADMIN_PASSWORD_WRONG;
            }
        }
        return admin;
    }

    @Override
    @JpaTransaction
    public boolean putAdminIfNotExists(Admin admin) {
        return inputIfNotExists(admin);
    }

    @Override
    @JpaTransaction
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return updateIfExists(newAdmin);
    }

    @Override
    @JpaTransaction
    public boolean deleteAdmin(String login) {
        Admin admin = getByNamedQueryStringArgument("getAdminByLogin", login, LOGIN);
        if (ADMIN_NOT_EXISTS == admin) {
            log.debug("Не получилось удалить обьект Admin по {}={}", LOGIN, login);
            return false;
        }
        return removeIfExists(admin);
    }

    @Override
    @JpaTransaction
    public boolean deleteAdmin(Admin admin) {
        return removeIfExists(admin);
    }
}
