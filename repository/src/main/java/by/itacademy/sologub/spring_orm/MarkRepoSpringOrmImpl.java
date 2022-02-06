package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@Slf4j
@Transactional
@Repository
public class MarkRepoSpringOrmImpl extends AbstractSpringOrm<Mark> implements MarkRepo {

    public MarkRepoSpringOrmImpl() {
        super(Mark.class, MARK_NOT_EXISTS);
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        return null;
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) return new HashSet<>();
        return student.getMarks();
    }

    @Override
    @Transactional(readOnly = true)
    public Mark getMark(int id) {
        return getByNamedQueryIntArgument("getMarkById", id, ID);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        Student student = getStudentById(studentId);
        if (STUDENT_NOT_EXISTS == student) {
            log.debug("Нельзя добавить оценку ученику, которого нет в БД");
            return false;
        }
        student.getMarks().add(mark);
        student = em.merge(student);
        boolean result = em.contains(student);
        log.debug("Результат изменения{} = {} ", student, result);
        return result;
    }

    @Override
    public boolean changeMark(Mark newValues) {
        return updateIfExists(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        Mark mark = getByNamedQueryIntArgument("getMarkById", id, ID);
        if (MARK_NOT_EXISTS == mark) {
            log.debug("Не получилось удалить обьект Mark по {}={}", ID, id);
            return false;
        }
        return removeIfExists(mark);
    }

    private Student getStudentById(int studentId) {
        Student student = em.createNamedQuery("getStudentById", Student.class)
                .setParameter(ID, studentId).getSingleResult();
        log.debug("Достали {} из бд", student);
        if (student == null) return STUDENT_NOT_EXISTS;
        return student;
    }
}