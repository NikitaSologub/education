package by.itacademy.sologub;

public interface CredentialRepo {
    Credential getCredentialIfExistsOrGetSpecialValue(String login);

    Credential getCredentialIfExistsOrGetSpecialValue(String login,String password);

    boolean putCredentialIfNotExists(String login, String password);

    boolean putCredentialIfNotExists(Credential credential);

    int putCredentialIfNotExistsAndGetId(Credential credential);

    boolean changeCredentialIfExists(String login, String password);

    boolean deleteCredentialIfExists(String login);
}