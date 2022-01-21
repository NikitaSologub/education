package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@Repository
public class SalaryRepoSpringOrmImpl extends AbstractSpringOrm<Salary> implements SalaryRepo {
    @Autowired
    public SalaryRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Salary.class, SALARY_NOT_EXISTS);
    }

    @Override
    @JpaTransaction
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher teacher = getTeacherById(teacherId);
        if (teacher == null) return new HashSet<>();
        return teacher.getSalaries();
    }

    @Override
    @JpaTransaction
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        Teacher teacher = getTeacherById(teacherId);
        if (teacher == null) return new HashSet<>();
        return teacher.getSalaries().stream()
                .filter(s -> s.getDate().isAfter(date))
                .collect(Collectors.toSet());
    }

    @Override
    @JpaTransaction
    public Salary getSalary(int id) {
        return getByNamedQueryIntArgument("getSalaryById", id, ID);
    }

    @Override
    @JpaTransaction
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        EntityManager em = helper.getEntityManager();
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
    @JpaTransaction
    public boolean changeSalary(Salary newValues) {
        return updateIfExists(newValues);
    }

    @Override
    @JpaTransaction
    public boolean deleteSalary(int id) {
        Salary salary = getByNamedQueryIntArgument("getSalaryById", id, ID);
        if (SALARY_NOT_EXISTS == salary) {
            log.debug("Не получилось удалить обьект Salary по {}={}", ID, id);
            return false;
        }
        return removeIfExists(salary);
    }

    private Teacher getTeacherById(int teacherId) {
        EntityManager em = helper.getEntityManager();
        TypedQuery<Teacher> typedQuery = em
                .createNamedQuery("getTeacherById", Teacher.class)
                .setParameter(ID, teacherId);
        Teacher teacher = typedQuery.getSingleResult();
        log.debug("Достали {} из бд", teacher);
        if (teacher == null) return TEACHER_NOT_EXISTS;
        return teacher;
    }
}