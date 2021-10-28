package by.itacademy.sologub;

import java.time.LocalDate;
import java.util.List;

public interface SalariesRepo {
    List<Salary> getAllSalariesByTeacherId(int teacherId);

    List<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date);

    Salary getSalary(int id);

    boolean putSalary(Salary salary);

    boolean changeSalary(int id, Salary newValues);

    boolean deleteSalary(int id);
}