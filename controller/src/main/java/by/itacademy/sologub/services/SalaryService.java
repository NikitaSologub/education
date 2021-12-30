package by.itacademy.sologub.services;

import by.itacademy.sologub.Salary;

import java.time.LocalDate;
import java.util.Set;

public interface SalaryService {
    Set<Salary> getAllSalariesByTeacherId(int teacherId);

    Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date);

    Salary getSalary(int id);

    boolean putSalaryToTeacher(Salary salary,int teacherId);

    boolean changeSalary(Salary newValues);

    boolean deleteSalary(int id);
}