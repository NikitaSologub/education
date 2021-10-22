package by.itacademy.sologub;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.Constants.SALARY_NOT_EXISTS;

public class SalariesRepoHardcodedImpl implements SalariesRepo {
    static int CURRENT_MAX_SALARY_ID = 10;
    private static SalariesRepoHardcodedImpl instance;
    private final Map<Integer, Salary> repo;

    private SalariesRepoHardcodedImpl(){
        repo = new HashMap<>();
    }

    public static SalariesRepoHardcodedImpl getInstance(){
        if (instance == null) {
            synchronized (SalariesRepoHardcodedImpl.class){
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
            return false;
        } else {
            salary.setId(key);
            repo.put(key, salary);
            return true;
        }
    }

    @Override
    public boolean deleteSalary(int id) {
        if (repo.containsKey(id)) {
            repo.remove(id);
            return true;
        } else {
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
            oldValues.setCoins(newValues.getCoins());
            oldValues.setDate(newValues.getDate());
            oldValues.setTeacherId(newValues.getTeacherId());
            return true;
        } else {
            return false;
        }
    }
}