package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Admin;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface AdminDataAccess extends CrudRepository<Admin, Integer> {
    Optional<Admin> findByCredentialId(int id);

    @Override
    Set<Admin> findAll();
}