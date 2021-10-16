package by.itacademy.sologub.factory;

import by.itacademy.sologub.*;

public final class ModelRepoFactoryHardcodeImpl implements ModelRepoFactory {
    private static final ModelRepoFactory modelRepoFactory;
    private static final CredentialRepo credentialRepo;
    private static final TeacherRepo teacherRepo;
    private static final StudentRepo studentRepo;
    private static final SalariesRepo salariesRepo;

    private ModelRepoFactoryHardcodeImpl() {
        //singleton
    }

    static {
        modelRepoFactory = new ModelRepoFactoryHardcodeImpl();
        credentialRepo = new CredentialRepoHardcodeImpl();
        teacherRepo = new TeacherRepoHardcodedImpl(credentialRepo);
        studentRepo = new StudentRepoHardcodedImpl(credentialRepo);
        salariesRepo = new SalariesRepoHardcodedImpl();
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

    public static ModelRepoFactory getInstance(){
        return modelRepoFactory;
    }
}