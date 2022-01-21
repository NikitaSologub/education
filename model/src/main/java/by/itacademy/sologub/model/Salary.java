package by.itacademy.sologub.model;

import by.itacademy.sologub.serializers.LocalDateDeserializer;
import by.itacademy.sologub.serializers.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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