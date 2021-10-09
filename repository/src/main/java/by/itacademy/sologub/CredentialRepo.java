package by.itacademy.sologub;

public interface CredentialRepo {
    Credential getCredentialIfExistsOrGetSpecialValue(String login);

    Credential getCredentialIfExistsOrGetSpecialValue(String login,String password);
}