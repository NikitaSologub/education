package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface StudentDataAccess extends CrudRepository<Student, Integer> {
    Optional<Student> findByCredentialId(int id);

    @Override
    Set<Student> findAll();
}