package by.itacademy.sologub;

import java.util.List;

public interface SubjectRepo {
    List<Subject> getSubjectsList();

    Subject getSubjectIfExistsOrGetSpecialValue(int id);

    boolean putSubjectIfNotExists(Subject subject);

    boolean changeSubjectsParametersIfExists(Subject subject);

    boolean deleteSubject(Subject subject);
}