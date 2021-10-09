package by.itacademy.sologub;

public class ModelRepoHardcodeImpl implements ModelRepo{
    private CredentialRepo credentialRepo = new CredentialRepoHardcodeImpl();

    ModelRepoHardcodeImpl() {
    }

    @Override
    public CredentialRepo getCredentialRepo() {
        return credentialRepo;
    }
}