package by.itacademy.sologub;

import java.util.Set;

public interface AdminRepo {
    Set<Admin> getAdminsList();

    Admin getAdminIfExistsOrGetSpecialValue(String login);

    Admin getAdminIfExistsOrGetSpecialValue(String login,String password);

    boolean putAdminIfNotExists(Admin admin);

    boolean changeAdminParametersIfExists(Admin newAdmin);

    boolean deleteAdmin(String login);

    boolean deleteAdmin(Admin admin);
}