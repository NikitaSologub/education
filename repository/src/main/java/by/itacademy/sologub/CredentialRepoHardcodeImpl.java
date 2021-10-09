package by.itacademy.sologub;

import java.util.HashMap;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.Constants.LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG;

public class CredentialRepoHardcodeImpl implements CredentialRepo {
    private static int CURRENT_MAX_CREDENTIAL_ID = 10;
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

    @Override
    public boolean putCredentialIfNotExists(String login, String password) {
        Credential tmp = repo.keySet().stream()
                .filter(cred -> cred.getLogin().equals(login))
                .findAny()
                .orElse(LOGIN_NOT_EXISTS);

        if (tmp == LOGIN_NOT_EXISTS) {
            Credential newCred = new Credential();
            newCred.setId(CURRENT_MAX_CREDENTIAL_ID++);
            newCred.setLogin(login);
            newCred.setPassword(password);
            repo.put(newCred, newCred);
            return true;
        }
        return false;
    }
}
