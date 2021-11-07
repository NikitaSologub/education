package by.itacademy.sologub.factory;

import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoHardcodeImpl;
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
    private static CredentialRepo credentialRepo;
    private static TeacherRepo teacherRepo;
    private static StudentRepo studentRepo;
    private static SalaryRepo salaryRepo;
    private static SubjectRepo subjectRepo;

    private ModelRepoFactoryHardcodeImpl() {
        credentialRepo = CredentialRepoHardcodeImpl.getInstance();
        teacherRepo = TeacherRepoHardcodedImpl.getInstance(credentialRepo);
        studentRepo = StudentRepoHardcodedImpl.getInstance(credentialRepo);
        salaryRepo = SalaryRepoHardcodedImpl.getInstance();
        subjectRepo = SubjectRepoHardcodedImpl.getInstance();
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
    public SalaryRepo getSalariesRepo() {
        return salaryRepo;
    }

    @Override
    public SubjectRepo getSubjectRepo() {
        return subjectRepo;
    }
}