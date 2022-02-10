package by.itacademy.sologub.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getStudentById", query = "select s from Student s where s.id=:id"),
        @NamedQuery(name = "getStudentByLogin", query = "select s from Student s where s.credential.login=:login")})
@DiscriminatorValue("STUDENT")
@Entity
public class Student extends User {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            orphanRemoval = true)
    @JoinColumn(name = "student_id", nullable = false)
    private Set<Mark> marks = new HashSet<>();

    public Student withId(int id) {
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