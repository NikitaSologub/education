package by.itacademy.sologub;

import java.util.HashMap;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.Constants.LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG;

public class CredentialRepoHardcodeImpl implements CredentialRepo {
    private static final int CURRENT_MAX_CREDENTIAL_ID = 1;
    private final Map<Credential, Credential> repo = new HashMap<>();

    public CredentialRepoHardcodeImpl() {
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        return repo.keySet().stream()
                .filter(cred -> cred.getLogin().equals(login))
                .findAny()
                .orElse(LOGIN_NOT_EXISTS);
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        return repo.keySet().stream()
                .filter(cred -> cred.getLogin().equals(login))
                .findAny()
                .orElse(LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG);
    }
}
