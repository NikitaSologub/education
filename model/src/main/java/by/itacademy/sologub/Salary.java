package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Salary extends AbstractEntity {
    private int coins;
    private LocalDate date;

    public Salary withId(int id) {
        setId(id);
        return this;
    }

    public Salary withCoins(int coins) {
        setCoins(coins);
        return this;
    }

    public Salary withDate(LocalDate date) {
        setDate(date);
        return this;
    }
}