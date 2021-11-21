package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Student extends User {
    private Set<Mark> marks = new HashSet<>();

    public Mark getMark(int id) {
        return marks.stream()
                .filter(mark -> mark.getId() == id)
                .findAny().orElse(MARK_NOT_EXISTS);
    }

    public void setMark(Mark mark) {
        marks.add(mark);
    }

    public Student withId(int id){
        setId(id);
        return this;
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

    public Student withCredential(Credential credential) {
        setCredential(credential);
        return this;
    }
}