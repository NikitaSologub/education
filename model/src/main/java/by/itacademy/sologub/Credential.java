package by.itacademy.sologub;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getCredentialByLogin",
                query = "select c from Credential c where c.login=:login"),
        @NamedQuery(name = "deleteCredentialByLogin",
                query = "delete from Credential c where c.login=:login"),
        @NamedQuery(name = "deleteCredentialById",
                query = "delete from Credential c where c.id=:id")})
@Entity
@Table(name = "credential")
public class Credential extends AbstractEntity {
    private String login;
    private String password;

    public Credential withId(int id) {
        setId(id);
        return this;
    }

    public Credential withLogin(String login) {
        setLogin(login);
        return this;
    }

    public Credential withPassword(String password) {
        setPassword(password);
        return this;
    }
}