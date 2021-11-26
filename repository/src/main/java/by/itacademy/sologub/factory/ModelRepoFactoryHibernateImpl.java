package by.itacademy.sologub.factory;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.AdminRepoHibernateImpl;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoHibernateImpl;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.TeacherRepo;
import org.hibernate.SessionFactory;

public final class ModelRepoFactoryHibernateImpl implements ModelRepoFactory {
    private static ModelRepoFactoryHibernateImpl instance;
    private static AdminRepoHibernateImpl adminRepo;
    private static CredentialRepoHibernateImpl credentialRepo;
    //тут будем добавлять ссылки на репозитории по типу private static xxxRepo;

    private ModelRepoFactoryHibernateImpl(SessionFactory sf) {
        adminRepo = AdminRepoHibernateImpl.getInstance(sf);
        credentialRepo = CredentialRepoHibernateImpl.getInstance(sf);
        //тут будем добавлять инициализацию репозиториев через XxxRepoHibernateImpl.getInstance(sf);
    }

    public static ModelRepoFactoryHibernateImpl getInstance(SessionFactory sf) {
        if (instance == null) {
            synchronized (ModelRepoFactoryHibernateImpl.class) {
                if (instance == null) {
                    instance = new ModelRepoFactoryHibernateImpl(sf);
                }
            }
        }
        return instance;
    }

    @Override
    public CredentialRepo getCredentialRepo() {
        return credentialRepo;
    }

    @Override
    public TeacherRepo getTeacherRepo() {
        return null;//todo - временная заглушка
    }

    @Override
    public StudentRepo getStudentRepo() {
        return null;//todo - временная заглушка
    }

    @Override
    public AdminRepo getAdminRepo() {
        return adminRepo;
    }

    @Override
    public SalaryRepo getSalariesRepo() {
        return null;//todo - временная заглушка
    }

    @Override
    public SubjectRepo getSubjectRepo() {
        return null;//todo - временная заглушка
    }

    @Override
    public GroupRepo getGroupRepo() {
        return null;//todo - временная заглушка
    }

    @Override
    public MarkRepo getMarkRepo() {
        return null;//todo - временная заглушка
    }
}