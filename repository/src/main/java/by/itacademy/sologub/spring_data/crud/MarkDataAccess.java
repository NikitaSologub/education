package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Subject;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface MarkDataAccess extends CrudRepository<Mark, Integer> {
    Set<Mark> findAllBySubject(Subject subject);
}