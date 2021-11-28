package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getTeacherById", query = "select t from Teacher t where t.id=:id"),
        @NamedQuery(name = "getTeacherByLogin", query = "select t from Teacher t where t.credential.login=:login")})
@DiscriminatorValue("TEACHER")
@Entity
public class Teacher extends User {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            orphanRemoval = true)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Set<Salary> salaries = new HashSet<>();

    public Teacher withId(int id) {
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