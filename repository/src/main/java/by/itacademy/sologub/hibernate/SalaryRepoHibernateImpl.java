package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@Repository
public class SalaryRepoHibernateImpl extends AbstractCrudRepoJpa<Salary> implements SalaryRepo {
    private static volatile TeacherRepoHibernateImpl teacherRepo;

    @Autowired
    public SalaryRepoHibernateImpl(SessionFactory sf, TeacherRepoHibernateImpl tr) {
        super(sf, Salary.class);
        teacherRepo = tr;
    }

    @Override
    protected Salary getEmptyObj() {
        return SALARY_NOT_EXISTS;
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        return t.getSalaries();
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        return t.getSalaries().stream()
                .filter(s -> s.getDate().isAfter(date))
                .collect(Collectors.toSet());
    }

    @Override
    public Salary getSalary(int id) {
        return getByNamedQueryIntArgument("getSalaryById", id, ID);
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        if (TEACHER_NOT_EXISTS == t) {
            log.debug("Нельзя добавить зарплату учителю, которого нет в БД");
            return false;
        }
        t.getSalaries().add(salary);
        return teacherRepo.change(t);
    }

    @Override //todo (вернёт true даже если нет изменений)
    public boolean changeSalary(Salary newValues) {
        return change(newValues);
    }

    @Override
    public boolean deleteSalary(int id) {
        Salary s = getSalary(id);
        if (SALARY_NOT_EXISTS == s) {
            log.debug("Не получилось удалить обьект Salary по {}={}", ID, id);
            return false;
        }
        return remove(s);
    }
}