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
    private static CredentialRepoHardcodeImpl instance;
    private final Map<Credential, Credential> repo;

    private CredentialRepoHardcodeImpl() {
        repo = new HashMap<>();
    }

    public static CredentialRepoHardcodeImpl getInstance() {
        if (instance == null) {
            synchronized (CredentialRepoHardcodeImpl.class) {
                if (instance == null) {
                    instance = new CredentialRepoHardcodeImpl();
                }
            }
        }
        return instance;
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
        log.debug("Пытаемся добавить обьект учётных данных по логину {} и паролю {} в репозиторий", login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            Credential newCred = new Credential()
                    .withId(CURRENT_MAX_CREDENTIAL_ID++)
                    .withLogin(login)
                    .withPassword(password);

            repo.put(newCred, newCred);
            log.info("Новый обьект учётных данных {} добавлен в репозиторий", newCred);
            return true;
        }
        log.info("Логин уже существует. Не удалось добавить обьект {}", cr);
        return false;
    }

    @Override
    public boolean putCredentialIfNotExists(Credential cred) {
        return putCredentialIfNotExists(cred.getLogin(), cred.getPassword());
    }

    @Override
    public int putCredentialIfNotExistsAndGetId(Credential credential) {
        Credential cr = getCredentialIfExistsOrGetSpecialValue(credential.getLogin());
        log.debug("Пытаемся добавить учётные данные логин={} пароль={} в репозиторий", credential.getLogin(), credential.getPassword());
        if (LOGIN_NOT_EXISTS == cr) {
            Credential newCred = new Credential()
                    .withId(CURRENT_MAX_CREDENTIAL_ID++)
                    .withLogin(credential.getLogin())
                    .withPassword(credential.getPassword());

            repo.put(newCred, newCred);
            log.info("Новый обьект учётных данных {} добавлен в репозиторий", newCred);
            return newCred.getId();
        }
        log.info("Логин уже существует. Не удалось добавить обьект {}", cr);
        return -1;
    }

    @Override
    public boolean changeCredentialIfExists(String login, String password) {
        Credential cr = getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся изменить пароль по логину {} в репозитории", login);

        if (LOGIN_NOT_EXISTS != cr) {
            cr.setPassword(password);
            log.info("Обьект учётных данных {} изменил пароль", cr);
            return true;
        }
        log.info("Не удалось изменить пароль. Учётных данных с логином={} не существует", login);
        return false;
    }

    @Override
    public boolean deleteCredentialIfExists(String login) {
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