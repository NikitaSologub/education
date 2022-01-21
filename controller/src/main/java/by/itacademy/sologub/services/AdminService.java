package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Admin;

import java.util.Set;

public interface AdminService {
    Set<Admin> getAdminsList();

    Admin getAdminIfExistsOrGetSpecialValue(String login);

    Admin getAdminIfExistsOrGetSpecialValue(String login, String password);

    boolean putAdminIfNotExists(Admin admin);

    boolean changeAdminParametersIfExists(Admin newAdmin);

    boolean deleteAdmin(String login);

    boolean deleteAdmin(Admin admin);
}