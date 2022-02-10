package by.itacademy.sologub.spring_data.crud;

import by.itacademy.sologub.model.Salary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Set;

public interface SalaryDataAccess extends CrudRepository<Salary, Integer> {
    @Query("select s from Salary s where s.date >= :date")
    Set<Salary> findAllWithDateAfter(@Param("date") LocalDate date);
}