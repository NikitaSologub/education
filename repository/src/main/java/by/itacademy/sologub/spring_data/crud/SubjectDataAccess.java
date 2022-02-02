package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectDataAccess extends CrudRepository<Subject, Integer> {
    @Override
    List<Subject> findAll();

    Optional<Subject> findByTitle(String title);
}