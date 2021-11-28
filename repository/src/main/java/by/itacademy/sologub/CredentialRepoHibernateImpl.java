package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
public class CredentialRepoHibernateImpl extends AbstractCrudRepoJpa<Credential> implements CredentialRepo {
    private static volatile CredentialRepoHibernateImpl instance;

    private CredentialRepoHibernateImpl(SessionFactory sf) {
        super(sf, Credential.class);
    }

    @Override
    protected Credential getEmptyObj() {
        return CREDENTIAL_NOT_EXISTS;
    }

    public static CredentialRepoHibernateImpl getInstance(SessionFactory sf) {
        if (instance == null) {
            synchronized (CredentialRepoHibernateImpl.class) {
                if (instance == null) {
                    instance = new CredentialRepoHibernateImpl(sf);
                }
            }
        }
        return instance;
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        Credential c = getByNamedQueryStringArgument("getCredentialByLogin", login, LOGIN);
        if (c == null) {
            log.debug("Нельзя получить Credential т.к. его не существует");
            return CREDENTIAL_NOT_EXISTS;
        }
        return c;
    }

    @Override
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        Credential c = getCredentialIfExistsOrGetSpecialValue(login);
        if (CREDENTIAL_NOT_EXISTS != c) {
            if (c.getPassword().equals(password)) {
                return c;
            } else {
                log.debug("Нельзя получить Credential т.к. пароль указан неверно");
                return PASSWORD_WRONG;
            }
        }
        return c;
    }

    @Override
    public boolean putCredentialIfNotExists(String login, String password) {
        return input(new Credential()
                .withLogin(login)
                .withPassword(password));
    }

    @Override
    public int putCredentialIfNotExistsAndGetId(Credential credential) {
        if (credential == null) {
            log.debug("Нельзя ложить в БД Null значения для Credential");
            return -1;
        }
        Credential c = getCredentialIfExistsOrGetSpecialValue(credential.getLogin());
        if (CREDENTIAL_NOT_EXISTS == c) {
            log.debug("Не удалось положить Credential в БД");
            return -1;
        }
        return c.getId();
    }

    @Override
    public boolean changeCredentialIfExists(String login, String password) {
        Credential c = getCredentialIfExistsOrGetSpecialValue(login);
        if (CREDENTIAL_NOT_EXISTS == c) {
            log.debug("Нельзя изменить Credential т.к. его не существует");
            return false;
        }
        return change(c);
    }

    @Override
    public boolean deleteCredentialIfExists(String login) {
        return executeByOneStringParameter(login, LOGIN, "deleteCredentialByLogin");
    }

    @Override
    public boolean deleteCredentialIfExists(int id) {
        return executeByOneStringParameter(String.valueOf(id), ID, "deleteCredentialById");
    }
}