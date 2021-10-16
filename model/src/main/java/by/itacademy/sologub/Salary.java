package by.itacademy.sologub;

import java.time.LocalDate;
import java.util.Objects;

public class Salary {
    private int id;
    private int coins;
    private int teacherId;
    private LocalDate date;

    public Salary() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int counts) {
        this.coins = counts;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Salary)) return false;
        Salary salary = (Salary) o;
        return getId() == salary.getId() && getCoins() == salary.getCoins() && getTeacherId() == salary.getTeacherId() && Objects.equals(getDate(), salary.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCoins(), getTeacherId(), getDate());
    }

    @Override
    public String toString() {
        return "Salary{id=" + id + ", counts=" + coins + ", teacherId=" + teacherId + ", date=" + date + '}';
    }
}