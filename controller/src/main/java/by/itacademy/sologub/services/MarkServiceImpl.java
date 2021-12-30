package by.itacademy.sologub.services;

import by.itacademy.sologub.Mark;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.Subject;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class MarkServiceImpl implements MarkService {
    private static MarkServiceImpl teacherService;
    private final MarkRepo repo;

    private MarkServiceImpl(MarkRepo markRepo) {
        this.repo = markRepo;
    }

    public static MarkServiceImpl getInstance(MarkRepo markRepo) {
        if (teacherService == null) {
            synchronized (MarkServiceImpl.class) {
                if (teacherService == null) {
                    teacherService = new MarkServiceImpl(markRepo);
                }
            }
        }
        return teacherService;
    }

    @Override
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        return repo.getAllMarksBySubjectAndStudentId(subject, studentId);
    }

    @Override
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        return repo.getAllMarksBySubject(subject);
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        return repo.getAllMarksByStudentId(studentId);
    }

    @Override
    public Mark getMark(int id) {
        return repo.getMark(id);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        return repo.putMarkToStudent(mark, studentId);
    }

    @Override
    public boolean changeMark(Mark newValues) {
        return repo.changeMark(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        return repo.deleteMark(id);
    }
}