package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@Slf4j
@Repository
public class MarkRepoHibernateImpl extends AbstractCrudRepoJpa<Mark> implements MarkRepo {
    private static volatile StudentRepoHibernateImpl studentRepo;

    @Autowired
    public MarkRepoHibernateImpl(SessionFactory sf, StudentRepoHibernateImpl tr) {
        super(sf, Mark.class);
        studentRepo = tr;
    }

    @Override
    protected Mark getEmptyObj() {
        return MARK_NOT_EXISTS;
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public Set<Mark> getAllMarksBySubjectAndStudentId(Subject subject, int studentId) {
        //попробовать нативный sql  GET_MARKS_BY_STUDENT_ID_AND_SUBJECT_ID   - ?
        return null;// дойду до реализации view Студентов и Учителей - сделаю этот метод
    }

    @Override//todo РЕАЛИЗОВАТЬ И ПРОТЕСТИРОВАТЬ (Этот в последнюю очередь)
    public Set<Mark> getAllMarksBySubject(Subject subject) {
        // именованным запросом - "getMarksBySubjectId" переводя в Set
        return null;// дойду до реализации view Студентов и Учителей - сделаю этот метод
    }

    @Override
    public Set<Mark> getAllMarksByStudentId(int studentId) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(studentId);
        return s.getMarks();
    }

    @Override
    public Mark getMark(int id) {
        return getByNamedQueryIntArgument("getMarkById", id, ID);
    }

    @Override
    public boolean putMarkToStudent(Mark mark, int studentId) {
        Student s = studentRepo.getStudentIfExistsOrGetSpecialValue(studentId);
        if (STUDENT_NOT_EXISTS == s) {
            log.debug("Нельзя добавить оценку ученику, которого нет в БД");
            return false;
        }
        s.getMarks().add(mark);
        return studentRepo.change(s);
    }

    @Override
    public boolean changeMark(Mark newValues) {
        return change(newValues);
    }

    @Override
    public boolean deleteMark(int id) {
        Mark m = getMark(id);
        if (MARK_NOT_EXISTS == m) {
            log.debug("Не получилось удалить обьект Salary по {}={}", ID, id);
            return false;
        }
        return remove(m);
    }
}