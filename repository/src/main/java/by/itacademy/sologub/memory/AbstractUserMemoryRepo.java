package by.itacademy.sologub.memory;

import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static by.itacademy.sologub.constants.ConstantObject.LOGIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
public abstract class AbstractUserMemoryRepo<T extends User> {
    private final CredentialRepoMemoryImpl credentialRepo;
    private final Map<Integer, T> users;

    protected AbstractUserMemoryRepo(CredentialRepoMemoryImpl credentialRepo) {
        this.credentialRepo = credentialRepo;
        users = new ConcurrentHashMap<>();
    }

    protected abstract int getCurrentMaxIdAndIncrement();

    protected abstract T getNotExists();

    protected abstract T getPasswordWrong();

    protected abstract String getType();

    protected Set<T> getUserSet() {
        return new HashSet<>(users.values());
    }

    protected T getUserIfExistsOrGetSpecialValue(int id) {
        return users.getOrDefault(id,getNotExists());
    }

    protected T getUserIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return getNotExists();
        } else {
            log.info("Логин существует, патаемся вернуть " + getType());
            return users.values().stream()
                    .filter(s -> s.getCredential().getLogin().equals(login))
                    .findAny().orElse(getNotExists());
        }
    }

    protected T getUserIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return getNotExists();
        } else if (PASSWORD_WRONG == cr) {
            log.info("Прароль не верен, возвращаем специальный обьект {}", getPasswordWrong());
            return getPasswordWrong();
        } else {
            log.info("Логин и пароль соответствуют, патаемся вернуть учителя");
            return users.values().stream()
                    .filter(u -> u.getCredential().getLogin().equals(login) &&
                            u.getCredential().getPassword().equals(password))
                    .findAny().orElse(getNotExists());
        }
    }

    protected boolean putUserIfNotExists(T user) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(user.getCredential().getLogin());
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("логина не существует, значит можно добавить нового " + getType());
            String login = user.getCredential().getLogin();
            String password = user.getCredential().getPassword();

            credentialRepo.putCredentialIfNotExists(login, password);
            cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);

            if (isExistsAndPasswordRight(cr)) {
                user.setId(getCurrentMaxIdAndIncrement());
                user.setCredential(cr);
                users.put(user.getId(), user);
                log.info("Удалось положить Credential, добавяем нового {}", user);
                return true;
            } else {
                log.info("Не удалось положить Credential, нельзя добавить нового " + getType());
            }
        }
        return false;
    }

    protected boolean changeUserParametersIfExists(T newU) {
        User oldU = users.get(newU.getId());
        log.debug("Пытаемся менять параметры " + getType() + " и Credential на новые");

        if (oldU != null && getNotExists() != oldU) {
            log.debug("Меняем параметры " + getType() + " и Credential на новые");

            oldU.setFirstname(newU.getFirstname());
            oldU.setLastname(newU.getLastname());
            oldU.setPatronymic(newU.getPatronymic());
            oldU.setDateOfBirth(newU.getDateOfBirth());
            boolean isChanged = credentialRepo.changeCredentialIfExists(newU.getCredential().getLogin(), newU.getCredential().getPassword());
            if (isChanged) {
                log.info("Учётная запись изменена, можно менять параметры " + getType());
            } else {
                log.info("Учётная запись не изменена, но параметры " + getType() + " будут изменены");
            }
            return true;
        } else {
            log.debug("Нельзя менять параметры " + getType() + " или Credential если их не существует в репозитории");
        }
        return false;
    }

    protected boolean deleteUser(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся проверить учетные данные обьекта " + getType() + " {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Нечего удалять");
        } else {
            User u = users.values().stream()
                    .filter(user -> user.getCredential().getLogin().equals(cr.getLogin()))
                    .findAny().orElse(getNotExists());
            if (getNotExists() != u) {
                users.remove(u.getId());
                credentialRepo.deleteCredentialIfExists(login);
                log.info("Объекты {} и Credentials удалены из репозиториев", getType());
                return true;
            } else {
                log.info("Объекта {} нет в базе. Нечего удалять", getType());
            }
        }
        return false;
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}