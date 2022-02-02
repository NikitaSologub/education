package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface TeacherDataAccess extends CrudRepository<Teacher, Integer> {
    Optional<Teacher> findByCredentialId(int id);

    @Override
    Set<Teacher> findAll();
}