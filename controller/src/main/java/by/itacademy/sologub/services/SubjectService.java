package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Subject;

import java.util.List;
import java.util.Set;

public interface SubjectService {
    List<Subject> getSubjectsList();

    Set<Subject> getSubjectsByGroupId(int groupId);

    Subject getSubjectIfExistsOrGetSpecialValue(int id);

    boolean putSubjectIfNotExists(Subject subject);

    boolean changeSubjectsParametersIfExists(Subject subject);

    boolean deleteSubject(Subject subject);
}