package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Credential extends AbstractEntity{
    private String login;
    private String password;

    public Credential withId(int id){
        setId(id);
        return this;
    }

    public Credential withLogin(String login){
        setLogin(login);
        return this;
    }

    public Credential withPassword(String password){
        setPassword(password);
        return this;
    }
}