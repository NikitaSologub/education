package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.spring_data.crud.MarkDataAccess;
import by.itacademy.sologub.spring_data.crud.StudentDataAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Repository
public class MarkRepoSpringDataImpl implements MarkRepo {
    private final StudentDataAccess studentDao;
    private final MarkDataAccess markDao;

    @Override
    @Transactional(readOnly = true)
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        return getAllMarksByStudentId(studentId).stream()
                .filter(mark -> mark.getSubject().equals(subject))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        return markDao.findAllBySubject(subject);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        Student student = studentDao.findById(studentId).orElse(STUDENT_NOT_EXISTS);
        if (STUDENT_NOT_EXISTS == student) {
            return new HashSet<>();
        }
        return student.getMarks();
    }

    @Override
    @Transactional(readOnly = true)
    public Mark getMark(int id) {
        return markDao.findById(id).orElse(MARK_NOT_EXISTS);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        Student student = studentDao.findById(studentId).orElse(STUDENT_NOT_EXISTS);
        log.info("Новая оценка  {}", mark);
        if (STUDENT_NOT_EXISTS != student) {
            log.info("Будем добавлять новую оценку студенту {}", student);
            student.getMarks().add(mark);
            studentDao.save(student);
            return true;
        }
        return false;
    }

    @Override
    public boolean changeMark(Mark newValues) {
        Mark newMarkInDb = markDao.save(newValues);
        return newMarkInDb.equals(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        markDao.deleteById(id);
        return !markDao.existsById(id);
    }
}