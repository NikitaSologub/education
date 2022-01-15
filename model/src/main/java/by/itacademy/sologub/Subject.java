package by.itacademy.sologub;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getSubjectById", query = "select s from Subject s where s.id=:id"),
        @NamedQuery(name = "deleteSubjectById", query = "delete from Subject s where s.id=:id")})
@Table(name = "subject")
@Entity
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