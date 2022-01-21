package by.itacademy.sologub.memory;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@Repository
public class SalaryRepoMemoryImpl implements SalaryRepo {
    static int CURRENT_MAX_SALARY_ID = 517;
    private static volatile TeacherRepoMemoryImpl teacherRepo;
    private final Map<Integer, Salary> repo;

    @Autowired
    public SalaryRepoMemoryImpl(TeacherRepoMemoryImpl teacherRepo) {
        repo = new ConcurrentHashMap<>();
        SalaryRepoMemoryImpl.teacherRepo = teacherRepo;
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        if (TEACHER_NOT_EXISTS == t) {
            return new HashSet<>();
        } else {
            return t.getSalaries();
        }
    }

    @Override
    public Set<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        if (date == null) {
            log.info("дата = null, возвращаем все значения");
            return getAllSalariesByTeacherId(teacherId);
        }
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        if (TEACHER_NOT_EXISTS == t) {
            return new HashSet<>();
        } else {
            return t.getSalaries().stream()
                    .filter(s -> s.getDate().isAfter(date))
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public boolean putSalaryToTeacher(Salary salary, int teacherId) {
        Integer key = CURRENT_MAX_SALARY_ID++;
        if (repo.containsKey(key)) {
            log.info("Зарплата {} не может быть добавлена в репозиторий. Такой id уже существует", salary);
            return false;
        } else {
            Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
            if (TEACHER_NOT_EXISTS == t) {
                log.info("Нельзя добавить з.п. учителю Нет учителя с таким id={}", teacherId);
                return false;
            } else {
                salary.setId(key);
                repo.put(key, salary);
                t.getSalaries().add(salary);
                log.info("Зарплата {} добавлена в репозиторий", salary);
                return true;
            }
        }
    }

    @Override
    public boolean deleteSalary(int id) {
        if (repo.containsKey(id)) {
            teacherRepo.getTeachersSet().stream()
                    .map(Teacher::getSalaries)
                    .forEach(set -> set.removeIf(salary -> salary.getId() == id));
            repo.remove(id);
            log.info("Зарплата по id {} удалена из репозитория", id);
            return true;
        } else {
            log.info("Не удалось удалить зарплату по id {} не существует такой зарплаты в репозитории", id);
            return false;
        }
    }

    @Override
    public Salary getSalary(int id) {
        return repo.getOrDefault(id, SALARY_NOT_EXISTS);
    }

    @Override
    public boolean changeSalary(Salary newValues) {
        if (repo.containsKey(newValues.getId())) {
            Salary oldValues = repo.get(newValues.getId());
            if (newValues.equals(oldValues)) {
                log.info("Нечего менять. Обьекты зарплат имеют эквивалентные значения");
                return false;
            } else {
                oldValues.withCoins(newValues.getCoins())
                        .withDate(newValues.getDate());
                log.info("Обьект зарплаты изменён и имеет вид: {}", oldValues);
                return true;
            }
        } else {
            log.info("Нечего менять.Обьект зарплаты который по id {} не существует в репозитории", newValues.getId());
            return false;
        }
    }
}