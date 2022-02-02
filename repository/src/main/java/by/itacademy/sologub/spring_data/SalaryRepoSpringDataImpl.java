package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.spring_data.crud.SalaryDataAccess;
import by.itacademy.sologub.spring_data.crud.TeacherDataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class SalaryRepoSpringDataImpl implements SalaryRepo {
    private final SalaryDataAccess salaryDao;
    private final TeacherDataAccess teacherDao;

    @Autowired
    public SalaryRepoSpringDataImpl(SalaryDataAccess salaryDao, TeacherDataAccess teacherDao) {
        this.salaryDao = salaryDao;
        this.teacherDao = teacherDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher teacher = teacherDao.findById(teacherId).orElse(TEACHER_NOT_EXISTS);
        if (TEACHER_NOT_EXISTS == teacher) {
            return new HashSet<>();
        }
        return teacher.getSalaries();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        return getAllSalariesByTeacherId(teacherId).stream()
                .filter(salary -> salary.getDate().isAfter(date))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Salary getSalary(int id) {
        return salaryDao.findById(id).orElse(SALARY_NOT_EXISTS);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        Teacher teacher = teacherDao.findById(teacherId).orElse(TEACHER_NOT_EXISTS);
        log.info("Новая зарплата  {}", salary);
        if (TEACHER_NOT_EXISTS != teacher) {
            log.info("Будем добавлять новую зарплату учителю {}", teacher);
            teacher.getSalaries().add(salary);
            teacherDao.save(teacher);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeSalary(Salary newValues) {
        Salary newSalaryInDb = salaryDao.save(newValues);
        return newSalaryInDb.equals(newValues);
    }

    @Override
    public boolean deleteSalary(int id) {
        salaryDao.deleteById(id);
        return !teacherDao.existsById(id);
    }
}