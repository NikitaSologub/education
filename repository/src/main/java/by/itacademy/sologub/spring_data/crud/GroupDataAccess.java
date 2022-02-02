package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Teacher;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GroupDataAccess extends CrudRepository<Group, Integer> {
    @Override
    List<Group> findAll();

    List<Group> findAllByTeacher(Teacher teacher);

    Optional<Group> findByTitle(String title);
}