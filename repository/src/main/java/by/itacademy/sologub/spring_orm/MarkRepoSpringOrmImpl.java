package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.spring_orm.aspects.JpaTransaction;
import by.itacademy.sologub.spring_orm.helper.EntityManagerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@Slf4j
@Repository
public class MarkRepoSpringOrmImpl extends AbstractSpringOrm<Mark> implements MarkRepo {
    @Autowired
    public MarkRepoSpringOrmImpl(EntityManagerHelper helper) {
        super(helper, Mark.class, MARK_NOT_EXISTS);
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
    @JpaTransaction
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) return new HashSet<>();
        return student.getMarks();
    }

    @Override
    @JpaTransaction
    public Mark getMark(int id) {
        return getByNamedQueryIntArgument("getMarkById", id, ID);
    }

    @Override
    @JpaTransaction
    public boolean putMarkToStudent(Mark mark, int studentId) {
        Student student = getStudentById(studentId);
        EntityManager em = helper.getEntityManager();
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
    @JpaTransaction
    public boolean deleteMark(int id) {
        Mark mark = getByNamedQueryIntArgument("getMarkById", id, ID);
        if (MARK_NOT_EXISTS == mark) {
            log.debug("Не получилось удалить обьект Mark по {}={}", ID, id);
            return false;
        }
        return removeIfExists(mark);
    }

    private Student getStudentById(int studentId) {
        EntityManager em = helper.getEntityManager();
        TypedQuery<Student> typedQuery = em
                .createNamedQuery("getStudentById", Student.class)
                .setParameter(ID, studentId);
        Student student = typedQuery.getSingleResult();
        log.debug("Достали {} из бд", student);
        if (student == null) return STUDENT_NOT_EXISTS;
        return student;
    }
}