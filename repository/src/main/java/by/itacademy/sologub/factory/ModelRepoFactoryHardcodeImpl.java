package by.itacademy.sologub.factory;

import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.AdminRepoHardcodedImpl;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoHardcodeImpl;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.GroupRepoHardcodedImpl;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.SalaryRepoHardcodedImpl;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.StudentRepoHardcodedImpl;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.SubjectRepoHardcodedImpl;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.TeacherRepoHardcodedImpl;

public final class ModelRepoFactoryHardcodeImpl implements ModelRepoFactory {
    private static ModelRepoFactoryHardcodeImpl instance;
    private static CredentialRepoHardcodeImpl credentialRepo;
    private static TeacherRepoHardcodedImpl teacherRepo;
    private static StudentRepoHardcodedImpl studentRepo;
    private static AdminRepoHardcodedImpl adminRepo;
    private static SalaryRepoHardcodedImpl salaryRepo;
    private static SubjectRepoHardcodedImpl subjectRepo;
    private static GroupRepoHardcodedImpl groupRepo;

    private ModelRepoFactoryHardcodeImpl() {
        credentialRepo = CredentialRepoHardcodeImpl.getInstance();
        teacherRepo = TeacherRepoHardcodedImpl.getInstance(credentialRepo);
        studentRepo = StudentRepoHardcodedImpl.getInstance(credentialRepo,GroupRepoHardcodedImpl.getInstance());
        adminRepo = AdminRepoHardcodedImpl.getInstance(credentialRepo);
        salaryRepo = SalaryRepoHardcodedImpl.getInstance(TeacherRepoHardcodedImpl.getInstance(credentialRepo));
        subjectRepo = SubjectRepoHardcodedImpl.getInstance(GroupRepoHardcodedImpl.getInstance());
        groupRepo = GroupRepoHardcodedImpl.getInstance();
    }

    public static ModelRepoFactoryHardcodeImpl getInstance() {
        if (instance == null) {
            synchronized (ModelRepoFactoryHardcodeImpl.class) {
                if (instance == null) {
                    instance = new ModelRepoFactoryHardcodeImpl();
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
        return null;//TODO- temporal
    }
}