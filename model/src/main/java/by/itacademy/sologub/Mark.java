package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getMarkById", query = "select m from Mark m where m.id=:id"),
        @NamedQuery(name = "getMarksBySubjectId", query = "select m from Mark m where m.subject.id=:id"),
        @NamedQuery(name = "deleteAllMarksBySubjectId", query = "delete from Mark m where m.subject.id=:id"),
        @NamedQuery(name = "deleteMarkById", query = "delete from Salary m where m.id=:id")})
@Table(name = "mark")
@Entity
public class Mark extends AbstractEntity {
    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    private int point;
    private LocalDate date;

    public Mark withId(int id) {
        setId(id);
        return this;
    }

    public Mark withSubject(Subject subject) {
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