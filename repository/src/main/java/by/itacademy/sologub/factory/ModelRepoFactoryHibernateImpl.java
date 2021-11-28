package by.itacademy.sologub.factory;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.AdminRepoHibernateImpl;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoHibernateImpl;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.GroupRepoHibernateImpl;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.MarkRepoHibernateImpl;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.SalaryRepoHibernateImpl;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.StudentRepoHibernateImpl;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.SubjectRepoHibernateImpl;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.TeacherRepoHibernateImpl;
import org.hibernate.SessionFactory;

public final class ModelRepoFactoryHibernateImpl implements ModelRepoFactory {
    private static ModelRepoFactoryHibernateImpl instance;
    private static AdminRepoHibernateImpl adminRepo;
    private static TeacherRepoHibernateImpl teacherRepo;
    private static SalaryRepoHibernateImpl salaryRepo;
    private static CredentialRepoHibernateImpl credentialRepo;
    private static StudentRepoHibernateImpl studentRepo;
    private static MarkRepoHibernateImpl markRepo;
    private static SubjectRepoHibernateImpl subjectRepo;
    private static GroupRepoHibernateImpl groupRepo;

    private ModelRepoFactoryHibernateImpl(SessionFactory sf) {
        credentialRepo = CredentialRepoHibernateImpl.getInstance(sf);
        adminRepo = AdminRepoHibernateImpl.getInstance(sf);
        teacherRepo = TeacherRepoHibernateImpl.getInstance(sf);
        salaryRepo = SalaryRepoHibernateImpl.getInstance(sf, teacherRepo);
        groupRepo = GroupRepoHibernateImpl.getInstance(sf);
        studentRepo = StudentRepoHibernateImpl.getInstance(sf, groupRepo);
        markRepo = MarkRepoHibernateImpl.getInstance(sf, studentRepo);
        subjectRepo = SubjectRepoHibernateImpl.getInstance(sf, groupRepo);
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
        return teacherRepo;
    }

    @Override
    public StudentRepo getStudentRepo() {
        return studentRepo;
    }

    @Override
    public AdminRepo getAdminRepo() {
        return adminRepo;
    }

    @Override
    public SalaryRepo getSalariesRepo() {
        return salaryRepo;
    }

    @Override
    public SubjectRepo getSubjectRepo() {
        return subjectRepo;
    }

    @Override
    public GroupRepo getGroupRepo() {
        return groupRepo;
    }

    @Override
    public MarkRepo getMarkRepo() {
        return markRepo;
    }
}