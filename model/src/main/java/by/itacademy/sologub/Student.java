package by.itacademy.sologub;

import by.itacademy.sologub.role.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Student extends User {
    private List<Mark> salaries;

    public List<Mark> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Mark> salaries) {
        this.salaries = salaries;
    }


    public Student withFirstname(String firstname) {
        setFirstname(firstname);
        return this;
    }

    public Student withLastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Student withPatronymic(String patronymic) {
        setPatronymic(patronymic);
        return this;
    }

    public Student withDateOfBirth(LocalDate date) {
        setDateOfBirth(date);
        return this;
    }

    public Student withRole(Role role) {
        setRole(role);
        return this;
    }

    public Student withCredential(Credential credential) {
        setCredential(credential);
        return this;
    }
}