package by.itacademy.sologub;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "getGroupById", query = "select g from Group g where g.id=:id"),
        @NamedQuery(name = "deleteGroupById", query = "delete from Group g where g.id=:id")})
@Entity
@Table(name = "group", schema = "public")
public class Group extends AbstractEntity {
    @Column(unique = true)
    private String title;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
    private String description;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "group_subject",
            joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "subject_id")})
    private Set<Subject> subjects = new HashSet<>();
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "group_student",
            joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private Set<Student> students = new HashSet<>();

    public boolean addStudent(Student s) {
        return this.students.add(s);
    }

    public boolean removeStudent(Student s) {
        return this.students.remove(s);
    }

    public boolean addSubject(Subject s) {
        return this.subjects.add(s);
    }

    public boolean removeSubject(Subject s) {
        return this.subjects.remove(s);
    }

    public Group withId(int id) {
        setId(id);
        return this;
    }

    public Group withTitle(String title) {
        setTitle(title);
        return this;
    }

    public Group withTeacher(Teacher teacher) {
        setTeacher(teacher);
        return this;
    }

    public Group withDescription(String description) {
        setDescription(description);
        return this;
    }

    public Group withStudents(Student... arr) {
        students.addAll(Arrays.asList(arr));
        return this;
    }

    public Group withSubjects(Subject... arr) {
        subjects.addAll(Arrays.asList(arr));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        if (!super.equals(o)) return false;
        Group group = (Group) o;
        return getTitle().equals(group.getTitle()) && Objects.equals(getTeacher(), group.getTeacher()) && getDescription().equals(group.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTitle(), getTeacher(), getDescription());
    }
}