package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.spring_data.crud.CredentialDataAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.PASSWORD_WRONG;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Repository
public class CredentialRepoSpringDataImpl implements CredentialRepo {
    private final CredentialDataAccess credentialDao;

    @Override
    @Transactional(readOnly = true)
    public Credential getCredentialIfExistsOrGetSpecialValue(String login) {
        return credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Credential getCredentialIfExistsOrGetSpecialValue(String login, String password) {
        Credential cred = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS != cred) {
            if (cred.getPassword().equals(password)) {
                return cred;
            } else {
                return PASSWORD_WRONG;
            }
        }
        return cred;
    }

    @Override
    public boolean putCredentialIfNotExists(String login, String password) {
        Credential isExists = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS == isExists) {
            Credential cred = credentialDao.save(new Credential()
                    .withLogin(login)
                    .withPassword(password));
            return credentialDao.existsById(cred.getId());
        }
        return false;
    }

    @Override
    public int putCredentialIfNotExistsAndGetId(Credential credential) {
        Credential isExists = credentialDao.findByLogin(credential.getLogin()).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS == isExists) {
            Credential cred = credentialDao.save(new Credential()
                    .withLogin(credential.getLogin())
                    .withPassword(credential.getPassword()));
            return cred.getId() > 0 ? cred.getId() : -1;
        }
        return -1;
    }

    @Override
    public boolean changeCredentialIfExists(String login, String password) {
        Credential credInDb = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS != credInDb) {
            Credential updatedCred = credentialDao.save(new Credential()
                    .withLogin(login)
                    .withPassword(password));
            return !updatedCred.equals(credInDb);
        }
        return false;
    }

    @Override
    public boolean deleteCredentialIfExists(String login) {
        Credential credInDb = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS != credInDb) {
            credentialDao.delete(credInDb);
            return !credentialDao.existsById(credInDb.getId());
        }
        return false;
    }

    @Override
    public boolean deleteCredentialIfExists(int id) {
        Credential credInDb = credentialDao.findById(id).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS != credInDb) {
            credentialDao.delete(credInDb);
            return !credentialDao.existsById(credInDb.getId());
        }
        return false;
    }
}