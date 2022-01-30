package by.itacademy.sologub.memory;

import by.itacademy.sologub.model.AbstractEntity;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.CredentialRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static by.itacademy.sologub.constants.Attributes.ID_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
@Repository
public class CredentialRepoMemoryImpl implements CredentialRepo {
    static int CURRENT_MAX_CREDENTIAL_ID = 137;
    private final Map<Integer, Credential> repo;

    @Autowired
    public CredentialRepoMemoryImpl() {
        repo = new ConcurrentHashMap<>();
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        if (login == null) {
            log.debug("Значение логина = null. Такого логина не существует");
            return LOGIN_NOT_EXISTS;
        }
        return repo.values().stream()
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

            repo.put(newCred.getId(), newCred);
            log.info("Новый обьект учётных данных {} добавлен в репозиторий", newCred);
            return true;
        }
        log.info("Логин уже существует. Не удалось добавить обьект {}", cr);
        return false;
    }

    @Override
    public int putCredentialIfNotExistsAndGetId(Credential credential) {
        if (putCredentialIfNotExists(credential.getLogin(), credential.getPassword())) {
            log.info("Credential {} добавлен. Возвращаем id", credential.getLogin());
            return repo.values().stream()
                    .filter(cr -> cr.getLogin().equals(credential.getLogin()))
                    .mapToInt(AbstractEntity::getId).findAny().orElse(ID_NOT_EXISTS);
        } else {
            log.info("Логин уже существует. Не удалось добавить обьект {}", credential.getLogin());
            return ID_NOT_EXISTS;
        }
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
            repo.remove(cr.getId());
            log.info("Обьект учётных данных {} успешно удалён из репозитория", cr);
            return true;
        }
    }

    @Override
    public boolean deleteCredentialIfExists(int id) {
        Credential cr = repo.remove(id);
        return cr != null;
    }
}