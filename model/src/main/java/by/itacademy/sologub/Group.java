package by.itacademy.sologub;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class Group extends AbstractEntity {
    private String title;
    private Teacher teacher;
    private String description;
    private Set<Subject> subjects = new HashSet<>();
    private Set<Student> students = new HashSet<>();

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

    public Group withStudents(Student ... arr){
        students.addAll(Arrays.asList(arr));
        return this;
    }

    public Group withSubjects(Subject ... arr){
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