package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getSalaryById", query = "select s from Salary s where s.id=:id"),
        @NamedQuery(name = "deleteSalaryById", query = "delete from Salary s where s.id=:id")})
@Table(name = "salary")
@Entity
public class Salary extends AbstractEntity {
    @Column(name = "coins_amount")
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