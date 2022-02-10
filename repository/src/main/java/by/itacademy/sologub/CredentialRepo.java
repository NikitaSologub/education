package by.itacademy.sologub;

import by.itacademy.sologub.model.Credential;

public interface CredentialRepo {
    Credential getCredentialIfExistsOrGetSpecialValue(String login);

    Credential getCredentialIfExistsOrGetSpecialValue(String login,String password);

    boolean putCredentialIfNotExists(String login, String password);

    int putCredentialIfNotExistsAndGetId(Credential credential);

    boolean changeCredentialIfExists(String login, String password);

    boolean deleteCredentialIfExists(String login);

    boolean deleteCredentialIfExists(int id);
}