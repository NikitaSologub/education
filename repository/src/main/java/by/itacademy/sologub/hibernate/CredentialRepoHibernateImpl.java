package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.CredentialRepo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
@Repository
public class CredentialRepoHibernateImpl extends AbstractCrudRepoJpa<Credential> implements CredentialRepo {
    @Autowired
    public CredentialRepoHibernateImpl(SessionFactory sf) {
        super(sf, Credential.class);
    }

    @Override
    protected Credential getEmptyObj() {
        return CREDENTIAL_NOT_EXISTS;
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