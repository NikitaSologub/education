package by.itacademy.sologub.services;

import by.itacademy.sologub.CredentialRepoHardcodeImpl;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.SalaryRepo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
public class SalaryServiceImpl implements SalaryService {
    private static SalaryServiceImpl salaryService;
    private final SalaryRepo repo;

    private SalaryServiceImpl(SalaryRepo salaryRepo) {
        this.repo = salaryRepo;
    }

    public static SalaryServiceImpl getInstance(SalaryRepo salaryRepo) {
        if (salaryService == null) {
            synchronized (SalaryServiceImpl.class) {
                if (salaryService == null) {
                    salaryService = new SalaryServiceImpl(salaryRepo);
                }
            }
        }
        return salaryService;
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        return repo.getAllSalariesByTeacherId(teacherId);
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        return repo.getAllSalariesByTeacherIdAfterDate(teacherId, date);
    }

    @Override
    public Salary getSalary(int id) {
        return repo.getSalary(id);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        return repo.putSalaryToTeacher(salary, teacherId);
    }

    @Override
    public boolean changeSalary(Salary newValues) {
        return repo.changeSalary(newValues);
    }

    @Override
    public boolean deleteSalary(int id) {
        return repo.deleteSalary(id);
    }
}
