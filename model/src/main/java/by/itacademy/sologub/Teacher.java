package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Teacher extends User {
    private Set<Salary> salaries = new HashSet<>();

    public Salary getSalary(int id) {
        return salaries.stream()
                .filter(salary -> salary.getId() == id)
                .findAny().orElse(SALARY_NOT_EXISTS);
    }

    public void setSalary(Salary salary) {
        salaries.add(salary);
    }

    public Teacher withId(int id){
        setId(id);
        return this;
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

    public Teacher withCredential(Credential credential) {
        setCredential(credential);
        return this;
    }
}