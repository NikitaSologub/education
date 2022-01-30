package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.SalaryRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class SalaryServiceImpl extends AbstractService implements SalaryService {
    private static final String PREFIX = "salaryRepo";
    private final Map<String, SalaryRepo> repoMap;
    private volatile SalaryRepo repo = null;

    @Autowired
    public SalaryServiceImpl(Map<String, SalaryRepo> repoMap) {
        this.repoMap = repoMap;
    }

    @PostConstruct
    public void init() {
        repo = repoMap.get(PREFIX + StringUtils.capitalize(type) + SUFFIX);
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
