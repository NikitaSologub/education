package by.itacademy.sologub;

import lombok.*;

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