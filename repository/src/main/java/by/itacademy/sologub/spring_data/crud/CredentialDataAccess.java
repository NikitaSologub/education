package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Credential;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialDataAccess extends CrudRepository<Credential, Integer> {
    Optional<Credential> findByLogin(String login);

    Optional<Credential> findByLoginAndPassword(String login, String password);
}