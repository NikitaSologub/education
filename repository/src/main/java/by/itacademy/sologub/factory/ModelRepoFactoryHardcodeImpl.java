package by.itacademy.sologub.factory;

import by.itacademy.sologub.*;

public final class ModelRepoFactoryHardcodeImpl implements ModelRepoFactory {
    private static ModelRepoFactoryHardcodeImpl instance;
    private static CredentialRepo credentialRepo;
    private static TeacherRepo teacherRepo;
    private static StudentRepo studentRepo;
    private static SalariesRepo salariesRepo;

    private ModelRepoFactoryHardcodeImpl() {
        credentialRepo = CredentialRepoHardcodeImpl.getInstance();
        teacherRepo = TeacherRepoHardcodedImpl.getInstance(credentialRepo);
        studentRepo = StudentRepoHardcodedImpl.getInstance(credentialRepo);
        salariesRepo = SalariesRepoHardcodedImpl.getInstance();
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
    public SalariesRepo getSalariesRepo() {
        return salariesRepo;
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
}