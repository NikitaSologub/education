package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
@Repository
public class CredentialRepoSpringOrmImpl extends AbstractSpringOrm<Credential> implements CredentialRepo {
    @Autowired
    public CredentialRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Credential.class, CREDENTIAL_NOT_EXISTS);
    }

    @Override
    @JpaTransaction
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        return getByNamedQueryStringArgument("getCredentialByLogin", login, LOGIN);
    }

    @Override
    @JpaTransaction
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        Credential c = getByNamedQueryStringArgument("getCredentialByLogin", login, LOGIN);
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
    @JpaTransaction
    public boolean putCredentialIfNotExists(String login, String password) {
        return inputIfNotExists(new Credential()
                .withLogin(login)
                .withPassword(password));
    }

    @Override
    @JpaTransaction
    public int putCredentialIfNotExistsAndGetId(Credential newC) {
        boolean isAdded = inputIfNotExists(newC);
        if (isAdded) {
            Credential fromDB = getByNamedQueryStringArgument("getCredentialByLogin", newC.getLogin(), LOGIN);
            return fromDB.getId();
        }
        return -1;
    }

    @Override
    @JpaTransaction
    public boolean changeCredentialIfExists(String login, String password) {
        Credential c = getByNamedQueryStringArgument("getCredentialByLogin", login, LOGIN);
        if (CREDENTIAL_NOT_EXISTS == c) {
            log.debug("Нельзя изменить Credential т.к. его не существует");
            return false;
        }
        return updateIfExists(c);
    }

    @Override
    @JpaTransaction
    public boolean deleteCredentialIfExists(String login) {
        if (login == null) return false;
        Credential credential = getByNamedQueryStringArgument("getCredentialByLogin", login, LOGIN);
        if (CREDENTIAL_NOT_EXISTS == credential) {
            log.debug("учётной записи с login={} нет в БД", login);
            return false;
        }
        return removeIfExists(credential);

    }

    @Override
    @JpaTransaction
    public boolean deleteCredentialIfExists(int id) {
        Credential credential = findOneByIdIfExists(id);
        if (CREDENTIAL_NOT_EXISTS == credential) {
            log.debug("учётной записи с id={} нет в БД", id);
            return false;
        }
        return removeIfExists(credential);
    }
}