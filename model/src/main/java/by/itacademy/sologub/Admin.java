package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Admin extends User {

    public Admin withId(int id){
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