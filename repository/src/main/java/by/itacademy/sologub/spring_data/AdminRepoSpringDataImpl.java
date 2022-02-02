package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.spring_data.crud.AdminDataAccess;
import by.itacademy.sologub.spring_data.crud.CredentialDataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;
import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class AdminRepoSpringDataImpl implements AdminRepo {
    private final AdminDataAccess adminDao;
    private final CredentialDataAccess credentialDao;

    @Autowired
    public AdminRepoSpringDataImpl(AdminDataAccess adminDao, CredentialDataAccess credentialDao) {
        this.adminDao = adminDao;
        this.credentialDao = credentialDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Admin> getAdminsList() {
        return adminDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        Credential cred = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS == cred) {
            return ADMIN_NOT_EXISTS;
        }
        return adminDao.findByCredentialId(cred.getId()).orElse(ADMIN_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        Admin admin = getAdminIfExistsOrGetSpecialValue(login);
        if (password.equals(admin.getCredential().getPassword())) {
            return admin;
        }
        return ADMIN_PASSWORD_WRONG;
    }

    @Override
    public boolean putAdminIfNotExists(Admin admin) {
        log.debug("добавляем нового админа в БД {}", admin);
        Admin adminInDb = adminDao.save(admin);
        return adminInDb.getId() != 0;
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        Admin oldAdminInDb = adminDao.findById(newAdmin.getId()).orElse(ADMIN_NOT_EXISTS);
        if (ADMIN_NOT_EXISTS != oldAdminInDb) {
            log.debug("корректируем параметры админа на новые {}", newAdmin);
            oldAdminInDb.setCredential(newAdmin.getCredential());
            oldAdminInDb.setFirstname(newAdmin.getFirstname());
            oldAdminInDb.setLastname(newAdmin.getLastname());
            oldAdminInDb.setPatronymic(newAdmin.getPatronymic());
            oldAdminInDb.setDateOfBirth(newAdmin.getDateOfBirth());
            Admin newAdminInDb = adminDao.save(oldAdminInDb);
            return newAdminInDb.equals(oldAdminInDb);
        }
        return false;
    }

    @Override
    public boolean deleteAdmin(String login) {
        Credential credential = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        Admin admin = adminDao.findByCredentialId(credential.getId()).orElse(ADMIN_NOT_EXISTS);
        if (ADMIN_NOT_EXISTS != admin) {
            adminDao.deleteById(admin.getId());
            return !adminDao.existsById(admin.getId());
        }
        return false;
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        adminDao.delete(admin);
        return !adminDao.existsById(admin.getId());
    }
}