package by.itacademy.sologub;

import java.time.LocalDate;
import java.util.Set;

public interface SalaryRepo {
    Set<Salary> getAllSalariesByTeacherId(int teacherId);

    Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date);

    Salary getSalary(int id);

    boolean putSalaryToTeacher(Salary salary,int teacherId);

    boolean changeSalary(Salary newValues);

    boolean deleteSalary(int id);
}