package by.itacademy.sologub;

import java.time.LocalDate;
import java.util.List;

public interface SalariesRepo {
    List<Salary> getAllSalariesByTeacherId(int teacherId);

    List<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date);

    boolean addSalary(Salary salary);

    boolean deleteSalary(int id);

    Salary getSalary(int id);

    boolean changeSalary(int id, Salary newValues);

}
