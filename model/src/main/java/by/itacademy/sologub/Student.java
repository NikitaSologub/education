package by.itacademy.sologub;

import java.util.List;

public class Student extends User {
    private List<Mark> salaries;

    public List<Mark> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Mark> salaries) {
        this.salaries = salaries;
    }
}