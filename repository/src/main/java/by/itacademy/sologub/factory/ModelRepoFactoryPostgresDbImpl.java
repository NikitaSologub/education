package by.itacademy.sologub.factory;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.AdminRepoPostgresImpl;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoPostgresImpl;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.GroupRepoPostgresImpl;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.MarkRepoPostgresImpl;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.SalaryRepoPostgresImpl;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.StudentRepoPostgresImpl;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.SubjectRepoPostgresImpl;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.TeacherRepoPostgresImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class ModelRepoFactoryPostgresDbImpl implements ModelRepoFactory {
    private static ModelRepoFactoryPostgresDbImpl instance;
    private static CredentialRepoPostgresImpl credentialRepo;
    private static TeacherRepoPostgresImpl teacherRepo;
    private static StudentRepoPostgresImpl studentRepo;
    private static AdminRepoPostgresImpl adminRepo;
    private static SalaryRepoPostgresImpl salaryRepo;
    private static SubjectRepoPostgresImpl subjectRepo;
    private static GroupRepoPostgresImpl groupRepo;
    private static MarkRepoPostgresImpl markRepo;

    private ModelRepoFactoryPostgresDbImpl(ComboPooledDataSource pool) {
        credentialRepo = CredentialRepoPostgresImpl.getInstance(pool);
        teacherRepo = TeacherRepoPostgresImpl.getInstance(pool);
        studentRepo = StudentRepoPostgresImpl.getInstance(pool);
        adminRepo = AdminRepoPostgresImpl.getInstance(pool);
        salaryRepo = SalaryRepoPostgresImpl.getInstance(pool);
        subjectRepo = SubjectRepoPostgresImpl.getInstance(pool);
        groupRepo = GroupRepoPostgresImpl.getInstance(pool);
        markRepo = MarkRepoPostgresImpl.getInstance(pool);
    }

    public static ModelRepoFactory getInstance(ComboPooledDataSource pool) {
        if (instance == null) {
            synchronized (ModelRepoFactoryPostgresDbImpl.class) {
                if (instance == null) {
                    instance = new ModelRepoFactoryPostgresDbImpl(pool);
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