package by.itacademy.sologub;

import java.util.List;

public class Teacher extends User {
    private List<Salary> salaries;

    public List<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
    }
}