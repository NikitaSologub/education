package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static by.itacademy.sologub.constants.Constants.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.Constants.PASSWORD_WRONG;

@Slf4j
public class CredentialRepoHardcodeImpl implements CredentialRepo {
    static int CURRENT_MAX_CREDENTIAL_ID = 10;
    private final Map<Credential, Credential> repo = new HashMap<>();

    public CredentialRepoHardcodeImpl() {
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        if (login == null) {
            log.debug("Значение логина = null. Такого логина не существует");
            return LOGIN_NOT_EXISTS;
        }
        return repo.keySet().stream()
                .filter(Objects::nonNull)
                .filter(cred -> cred.getLogin().equals(login))
                .findAny()
                .orElse(LOGIN_NOT_EXISTS);
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            log.debug("Логина не существует. Возвращаем специальный объект {}", LOGIN_NOT_EXISTS);
            return LOGIN_NOT_EXISTS;
        } else {
            if (cr.getPassword().equals(password)) {
                log.debug("Логин и пароль есть в репозитории. Возвращаем {}", cr);
                return cr;
            } else {
                log.debug("Пароль неверен. Возвращаем специальный объект {}", PASSWORD_WRONG);
                return PASSWORD_WRONG;
            }
        }
    }

    @Override
    public boolean putCredentialIfNotExists(String login, String password) {
        Credential cr = getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся добавить обьект учётных данных {} в репозиторий", cr);
        if (cr == LOGIN_NOT_EXISTS) {
            Credential newCred = new Credential();
            newCred.setId(CURRENT_MAX_CREDENTIAL_ID++);
            newCred.setLogin(login);
            newCred.setPassword(password);
            repo.put(newCred, newCred);
            log.info("Новый обьект учётных данных {} добавлен в репозиторий", newCred);
            return true;
        }
        log.info("Логин уже существует. Не удалось добавить обьект {}", cr);
        return false;
    }

    @Override
    public boolean removeCredentialIfExists(String login) {
        Credential cr = getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся удалить обьект учётных данных {} из репозитория", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Обьекта учётных данных {} не существует", cr);
            return false;
        } else {
            repo.remove(cr);
            log.info("Обьект учётных данных {} успешно удалён из репозитория", cr);
            return true;
        }
    }
}