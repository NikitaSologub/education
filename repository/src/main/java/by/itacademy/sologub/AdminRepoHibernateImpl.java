package by.itacademy.sologub;

import org.hibernate.SessionFactory;

import java.util.Set;

public class AdminRepoHibernateImpl extends AbstractCrudRepoJpa<Admin> implements AdminRepo {
    private static volatile AdminRepoHibernateImpl instance;

    private AdminRepoHibernateImpl(SessionFactory sf) {
        super(sf, Admin.class);
    }

    public static AdminRepoHibernateImpl getInstance(SessionFactory sf) {
        if (instance == null) {
            synchronized (AdminRepoHibernateImpl.class) {
                if (instance == null) {
                    instance = new AdminRepoHibernateImpl(sf);
                }
            }
        }
        return instance;
    }

    //todo - getEntityManager(); - будем вызывать из BaseCrudRepoJpa

    @Override
    public Set<Admin> getAdminsList() {
        return null;//todo - ЗАГЛУШКА
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login) {
        return null;//todo - ЗАГЛУШКА
    }

    @Override
    public Admin getAdminIfExistsOrGetSpecialValue(String login, String password) {
        return null;//todo - ЗАГЛУШКА
    }

    @Override
    public boolean putAdminIfNotExists(Admin admin) {
        return false;//todo - ЗАГЛУШКА
    }

    @Override
    public boolean changeAdminParametersIfExists(Admin newAdmin) {
        return false;//todo - ЗАГЛУШКА
    }

    @Override
    public boolean deleteAdmin(String login) {
        return false;//todo - ЗАГЛУШКА
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        return false;//todo - ЗАГЛУШКА
    }
}