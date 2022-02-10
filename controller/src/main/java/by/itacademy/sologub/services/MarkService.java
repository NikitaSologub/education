package by.itacademy.sologub.services;

import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Subject;

import java.util.Set;

public interface MarkService {
    Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId);

    Set<Mark> getAllMarksBySubject(Subject subject);

    Set<Mark> getAllMarksByStudentId(int studentId);

    Mark getMark(int id);

    boolean putMarkToStudent(Mark mark, int studentId);

    boolean changeMark(Mark newValues);

    boolean deleteMark(int id);
}