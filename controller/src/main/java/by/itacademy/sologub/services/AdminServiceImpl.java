package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.AdminRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class AdminServiceImpl extends AbstractService implements AdminService {
    private static final String PREFIX = "adminRepo";
    private final Map<String, AdminRepo> repoMap;
    private volatile AdminRepo repo = null;

    @Autowired
    public AdminServiceImpl(Map<String, AdminRepo> repoMap) {
        this.repoMap = repoMap;
    }

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
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