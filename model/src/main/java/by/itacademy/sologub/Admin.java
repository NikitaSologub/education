package by.itacademy.sologub;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getAdminByLogin",
                query = "select a from Admin a where a.credential.login=:login"),
        @NamedQuery(name = "deleteAdminByLogin",//этот запрос не удалит Credential при удалении Admin
                query = "delete from Admin a where a.credential IN (select c FROM Credential c where c.login=:login)")})
@DiscriminatorValue("ADMIN")
@Entity
public class Admin extends User {
    public Admin withId(int id) {
        setId(id);
        return this;
    }

    public Admin withFirstname(String firstname) {
        setFirstname(firstname);
        return this;
    }

    public Admin withLastname(String lastname) {
        setLastname(lastname);
        return this;
    }

    public Admin withPatronymic(String patronymic) {
        setPatronymic(patronymic);
        return this;
    }

    public Admin withDateOfBirth(LocalDate date) {
        setDateOfBirth(date);
        return this;
    }

    public Admin withCredential(Credential credential) {
        setCredential(credential);
        return this;
    }
}