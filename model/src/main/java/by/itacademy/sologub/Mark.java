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
public class Mark extends AbstractEntity {
    private Subject subject;
    private int point;
    private LocalDate date;

    public Mark withId(int id) {
        setId(id);
        return this;
    }

    public Mark withSubject(Subject subject){
        setSubject(subject);
        return this;
    }

    public Mark withDate(LocalDate date) {
        setDate(date);
        return this;
    }

    public Mark withPoint(int point) {
        setPoint(point);
        return this;
    }
}