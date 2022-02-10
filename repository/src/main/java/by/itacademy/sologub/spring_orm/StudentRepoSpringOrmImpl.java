package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
@Transactional
@Repository
public class StudentRepoSpringOrmImpl extends AbstractSpringOrm<Student> implements StudentRepo {

    public StudentRepoSpringOrmImpl() {
        super(Student.class, STUDENT_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsSet() {
        List<Student> students = findAll();
        log.info("возвращаем {}", students);
        return new HashSet<>(students);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsByGroupId(int groupId) {
        Group group = getGroupById(groupId);
        if (GROUP_NOT_EXISTS == group) {
            log.info("группы по id={} нет в БД", groupId);
            return new HashSet<>();
        }
        log.info("Возвращаем Set<Student> из БД");
        return group.getStudents();
    }

    private Group getGroupById(int groupId) {
        Group group = em.createNamedQuery("getGroupById", Group.class)
                .setParameter(ID, groupId)
                .getSingleResult();
        log.debug("Достали {} из бд", group);
        if (group == null) return GROUP_NOT_EXISTS;
        return group;
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return getByNamedQueryIntArgument("getStudentById", id, ID);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        return getByNamedQueryStringArgument("getStudentByLogin", login, LOGIN);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Student s = getByNamedQueryStringArgument("getStudentByLogin", login, LOGIN);
        if (STUDENT_NOT_EXISTS != s) {
            if (s.getCredential().getPassword().equals(password)) {
                return s;
            } else {
                log.debug("Не получилось получить обьект Student по {}={}. Пароль не верен", LOGIN, login);
                return STUDENT_PASSWORD_WRONG;
            }
        }
        return s;
    }

    @Override
    public boolean putStudentIfNotExists(Student student) {
        return inputIfNotExists(student);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        Student old = getByNamedQueryIntArgument("getStudentById", newStudent.getId(), ID);
        Set<Mark> marks = old.getMarks();
        newStudent.setMarks(marks);
        return updateIfExists(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        Student student = getByNamedQueryStringArgument("getStudentByLogin", login, LOGIN);
        if (STUDENT_NOT_EXISTS == student) {
            log.debug("Не получилось удалить обьект Student по {}={}", LOGIN, login);
            return false;
        }
        return removeIfExists(student);
    }

    @Override
    public boolean deleteStudent(Student student) {
        return removeIfExists(student);
    }
}