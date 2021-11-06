package by.itacademy.sologub;

import java.util.List;

public interface AdminRepo {
    List<Admin> getAdminsList();

    Admin getAdminIfExistsOrGetSpecialValue(String login);

    Admin getAdminIfExistsOrGetSpecialValue(String login,String password);

    boolean putAdminIfNotExists(Admin teacher);

    boolean changeAdminParametersIfExists(Admin newAdmin);

    boolean deleteAdmin(String login);

    boolean deleteAdmin(Admin Admin);
}