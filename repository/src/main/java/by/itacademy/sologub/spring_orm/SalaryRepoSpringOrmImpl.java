package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class SalaryRepoSpringOrmImpl extends AbstractSpringOrm<Salary> implements SalaryRepo {

    public SalaryRepoSpringOrmImpl() {
        super(Salary.class, SALARY_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        if (teacher == null) return new HashSet<>();
        return teacher.getSalaries();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        Teacher teacher = getTeacherById(teacherId);
        if (teacher == null) return new HashSet<>();
        return teacher.getSalaries().stream()
                .filter(s -> s.getDate().isAfter(date))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Salary getSalary(int id) {
        return getByNamedQueryIntArgument("getSalaryById", id, ID);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        if (teacher == TEACHER_NOT_EXISTS) {
            log.debug("Нельзя добавить зарплату учителю, которого нет в БД");
            return false;
        }
        teacher.getSalaries().add(salary);
        teacher = em.merge(teacher);
        boolean result = em.contains(teacher);
        log.debug("Результат изменения{} = {} ", teacher, result);
        return result;
    }

    @Override
    public boolean changeSalary(Salary newValues) {
        return updateIfExists(newValues);
    }

    @Override
    public boolean deleteSalary(int id) {
        Salary salary = getByNamedQueryIntArgument("getSalaryById", id, ID);
        if (SALARY_NOT_EXISTS == salary) {
            log.debug("Не получилось удалить обьект Salary по {}={}", ID, id);
            return false;
        }
        return removeIfExists(salary);
    }

    private Teacher getTeacherById(int teacherId) {
        Teacher teacher = em.createNamedQuery("getTeacherById", Teacher.class)
                .setParameter(ID, teacherId).getSingleResult();
        log.debug("Достали {} из бд", teacher);
        if (teacher == null) return TEACHER_NOT_EXISTS;
        return teacher;
    }
}