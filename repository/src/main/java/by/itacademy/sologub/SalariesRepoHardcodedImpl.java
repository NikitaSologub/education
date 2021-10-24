package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.Constants.SALARY_NOT_EXISTS;

@Slf4j
public class SalariesRepoHardcodedImpl implements SalariesRepo {
    static int CURRENT_MAX_SALARY_ID = 10;
    private static SalariesRepoHardcodedImpl instance;
    private final Map<Integer, Salary> repo;

    private SalariesRepoHardcodedImpl() {
        repo = new HashMap<>();
    }

    public static SalariesRepoHardcodedImpl getInstance() {
        if (instance == null) {
            synchronized (SalariesRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new SalariesRepoHardcodedImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public List<Salary> getAllSalariesByTeacherId(int teacherId) {
        return repo.values().stream()
                .filter(salary -> salary.getTeacherId() == teacherId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Salary> getAllSalariesByTeacherIdAfterDate(int teacherId, LocalDate date) {
        if (date == null) {
            log.info("дата = null, возвращаем все значения");
            return getAllSalariesByTeacherId(teacherId);
        }
        return repo.values().stream()
                .filter(salary -> salary.getTeacherId() == teacherId)
                .filter(salary -> salary.getDate().isAfter(date))
                .collect(Collectors.toList());
    }

    @Override
    public boolean addSalary(Salary salary) {
        Integer key = CURRENT_MAX_SALARY_ID++;
        if (repo.containsKey(key)) {
            log.info("Зарплата {} не может быть добавлена в репозиторий. Такой id уже существует", salary);
            return false;
        } else {
            salary.setId(key);
            repo.put(key, salary);
            log.info("Зарплата {} добавлена в репозиторий", salary);
            return true;
        }
    }

    @Override
    public boolean deleteSalary(int id) {
        if (repo.containsKey(id)) {
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
    public boolean changeSalary(int id, Salary newValues) {
        if (repo.containsKey(id)) {
            Salary oldValues = repo.get(id);
            if (newValues.equals(oldValues)) {
                log.info("Нечего менять. Обьекты зарплат имеют эквивалентные значения");
                return false;
            } else {
                oldValues.withCoins(newValues.getCoins())
                        .withDate(newValues.getDate())
                        .withTeacherId(newValues.getTeacherId());
                log.info("Обьект зарплаты изменён и имеет вид: {}", oldValues);
                return true;
            }
        } else {
            log.info("Нечего менять.Обьект зарплаты который по id {} не существует в репозитории", id);
            return false;
        }
    }
}