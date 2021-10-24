package by.itacademy.sologub;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Salary extends AbstractEntity {
    private int coins;
    private int teacherId;
    private LocalDate date;

    public Salary withCoins(int coins) {
        setCoins(coins);
        return this;
    }

    public Salary withTeacherId(int teacherId) {
        setTeacherId(teacherId);
        return this;
    }

    public Salary withDate(LocalDate date) {
        setDate(date);
        return this;
    }
}