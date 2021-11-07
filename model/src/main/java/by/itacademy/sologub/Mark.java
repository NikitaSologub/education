package by.itacademy.sologub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Mark extends AbstractEntity {
    private int studentId;
    //продумать бизнес логику
//    private int subjectId;
//    private LocalDate date;
    private int point;

    public Mark withStudentId(int studentId) {
        setStudentId(studentId);
        return this;
    }

    public Mark withPoint(int point) {
        setPoint(point);
        return this;
    }
}