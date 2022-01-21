package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Salary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AverageSalaryServiceImpl implements AverageSalaryService {
    private final SalaryService salaryService;

    @Autowired
    public AverageSalaryServiceImpl(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @Override
    public String getAverageSalary(int teacherId) {
        double average = salaryService.getAllSalariesByTeacherId(teacherId).stream()
                .mapToInt(Salary::getCoins)
                .average().orElse(0.0);
        return round(average);
    }

    private String round(double val) {
        return String.format("%.2f", val);
    }
}