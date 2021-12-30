package by.itacademy.sologub.services;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.AdminRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class AdminServiceImpl implements AdminService {
    private static AdminServiceImpl teacherService;
    private final AdminRepo repo;

    private AdminServiceImpl(AdminRepo adminRepo) {
        this.repo = adminRepo;
    }

    public static AdminServiceImpl getInstance(AdminRepo adminRepo) {
        if (teacherService == null) {
            synchronized (AdminServiceImpl.class) {
                if (teacherService == null) {
                    teacherService = new AdminServiceImpl(adminRepo);
                }
            }
        }
        return teacherService;
    }

    @Override
    public Set<Admin> getAdminsList() {
        return repo.getAdminsList();
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        return repo.getAdminIfExistsOrGetSpecialValue(login);
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        return repo.getAdminIfExistsOrGetSpecialValue(login, password);
    }

    @Override
    public boolean putAdminIfNotExists(Admin a) {
        return repo.putAdminIfNotExists(a);
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return repo.changeAdminParametersIfExists(newAdmin);
    }

    @Override
    public boolean deleteAdmin(String login) {
        return repo.deleteAdmin(login);
    }

    @Override
    public boolean deleteAdmin(Admin a) {
        return repo.deleteAdmin(a);
    }
}