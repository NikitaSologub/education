package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j //TODO -менять все методы класса с помощью имплементации TeacherRepoHardcodedImpl
public class SalaryRepoHardcodedImpl implements SalaryRepo {
    static int CURRENT_MAX_SALARY_ID = 517;
    private static volatile SalaryRepoHardcodedImpl instance;
    private static volatile TeacherRepoHardcodedImpl teacherRepo;
    private final Map<Integer, Salary> repo;

    private SalaryRepoHardcodedImpl(TeacherRepoHardcodedImpl teacherRepo) {
        repo = new ConcurrentHashMap<>();
        SalaryRepoHardcodedImpl.teacherRepo = teacherRepo;
    }

    public static SalaryRepoHardcodedImpl getInstance(TeacherRepoHardcodedImpl teacherRepo) {
        if (instance == null) {
            synchronized (SalaryRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new SalaryRepoHardcodedImpl(teacherRepo);
                }
            }
        }
        return instance;
    }

    @Override//OK
    public Set<Salary> getAllSalariesByTeacherId(int teacherId) {
        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherId);
        if (TEACHER_NOT_EXISTS == t) {
            return new HashSet<>();
        } else {
            return t.getSalaries();
        }
//        return repo.values().stream()
//                .filter(salary -> salary.getTeacherId() == teacherId)
//                .collect(Collectors.toSet());
    }

    @Override//OK
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
//        return repo.values().stream()
//                .filter(salary -> salary.getTeacherId() == teacherId)
//                .filter(salary -> salary.getDate().isAfter(date))
//                .collect(Collectors.toSet());
    }

//    @Override//OK
//    public boolean putSalary(Salary salary) {//TODO - УДАЛИТЬ ПОТОМ ЭТОТ МЕТОД
//        throw new IllegalStateException("Этот метод удалить потом вообще");
//        Integer key = CURRENT_MAX_SALARY_ID++;
//        if (repo.containsKey(key)) {
//            log.info("Зарплата {} не может быть добавлена в репозиторий. Такой id уже существует", salary);
//            return false;
//        } else {
//            salary.setId(key);
//            repo.put(key, salary);
//            log.info("Зарплата {} добавлена в репозиторий", salary);
//            return true;
//        }
//    }

    @Override//OK
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
                t.setSalary(salary);
                log.info("Зарплата {} добавлена в репозиторий", salary);
                return true;
            }
        }
    }

    @Override//OK
    public boolean deleteSalary(int id) {//TODO- при удалении обьекта
        if (repo.containsKey(id)) {
            teacherRepo.getTeachersList().stream()
                    .map(Teacher::getSalaries)//TODO- возможно это будет не нужно, тогда закоментирую
//                    .forEach(set -> set.removeIf(Objects::isNull));
                    .forEach(set -> set.removeIf(salary -> salary.getId() == id));
            repo.remove(id);
            log.info("Зарплата по id {} удалена из репозитория", id);
            return true;
        } else {
            log.info("Не удалось удалить зарплату по id {} не существует такой зарплаты в репозитории", id);
            return false;
        }
    }

    @Override//OK
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
//                        .withTeacherId(newValues.getTeacherId());
                log.info("Обьект зарплаты изменён и имеет вид: {}", oldValues);
                return true;
            }
        } else {
            log.info("Нечего менять.Обьект зарплаты который по id {} не существует в репозитории", newValues.getId());
            return false;
        }
    }
}