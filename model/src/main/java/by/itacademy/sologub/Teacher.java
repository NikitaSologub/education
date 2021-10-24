package by.itacademy.sologub;

import by.itacademy.sologub.role.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static by.itacademy.sologub.constants.Constants.SALARY_NOT_EXISTS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends User {
    private List<Salary> salaries = new ArrayList<>();

    public Salary getSalary(int id) {
        return salaries.stream()
                .filter(salary -> salary.getId() == id)
                .findAny().orElse(SALARY_NOT_EXISTS);
    }

    public void setSalary(Salary salary) {
        salaries.add(salary);
    }

    public Teacher withFirstname(String firstname) {
        setFirstname(firstname);
        return this;
    }

    public Teacher withLastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Teacher withPatronymic(String patronymic) {
        setPatronymic(patronymic);
        return this;
    }

    public Teacher withDateOfBirth(LocalDate date) {
        setDateOfBirth(date);
        return this;
    }

    public Teacher withRole(Role role) {
        setRole(role);
        return this;
    }

    public Teacher withCredential(Credential credential) {
        setCredential(credential);
        return this;
    }
}