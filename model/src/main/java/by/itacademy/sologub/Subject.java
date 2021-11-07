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
public class Subject extends AbstractEntity {
    private String title;

    public Subject withId(int id) {
        setId(id);
        return this;
    }

    public Subject withTitle(String title) {
        setTitle(title);
        return this;
    }
}