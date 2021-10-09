package by.itacademy.sologub.factory;

import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.CredentialRepoHardcodeImpl;

public final class ModelRepoFactoryHardcodeImpl implements ModelRepoFactory {
    private static final ModelRepoFactory modelRepoFactory;
    private static final CredentialRepo credentialRepo;

    private ModelRepoFactoryHardcodeImpl() {
        //singleton
    }

    static {
        modelRepoFactory = new ModelRepoFactoryHardcodeImpl();
        credentialRepo = new CredentialRepoHardcodeImpl();
    }

    @Override
    public CredentialRepo getCredentialRepo() {
        return credentialRepo;
    }

    public static ModelRepoFactory getInstance(){
        return modelRepoFactory;
    }
}