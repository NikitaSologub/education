package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
@Repository
public class StudentRepoHibernateImpl extends AbstractCrudRepoJpa<Student> implements StudentRepo {
    private static volatile GroupRepoHibernateImpl groupRepo;

    @Autowired
    public StudentRepoHibernateImpl(SessionFactory sf, GroupRepoHibernateImpl gr) {
        super(sf, Student.class);
        groupRepo = gr;
    }

    @Override
    protected Student getEmptyObj() {
        return STUDENT_NOT_EXISTS;
    }

    @Override
    public Set<Student> getStudentsSet() {
        return new HashSet<>(getAll());
    }

    @Override
    public Set<Student> getStudentsByGroupId(int groupId) {
        Group g = groupRepo.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == g) return new HashSet<>();
        return g.getStudents();
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        Student t = getByNamedQueryIntArgument("getStudentById", id, ID);
        return getObjOrSpecialEmpty(t, String.valueOf(id), ID);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        Student t = getByNamedQueryStringArgument("getStudentByLogin", login, LOGIN);
        return getObjOrSpecialEmpty(t, login, LOGIN);
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Student s = getStudentIfExistsOrGetSpecialValue(login);
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

    private Student getObjOrSpecialEmpty(Student t, String value, String columnName) {
        if (t == null) {
            log.debug("Не получилось получить обьект Student по {}={}", columnName, value);
            return STUDENT_NOT_EXISTS;
        }
        return t;
    }

    @Override
    public boolean putStudentIfNotExists(Student student) {
        return input(student);
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        Set<Mark> marks = getStudentIfExistsOrGetSpecialValue(newStudent.getId()).getMarks();
        newStudent.setMarks(marks);
        return change(newStudent);
    }

    @Override
    public boolean deleteStudent(String login) {
        Student s = getStudentIfExistsOrGetSpecialValue(login);
        if (STUDENT_NOT_EXISTS == s) {
            log.debug("Не получилось удалить обьект Student по {}={}", LOGIN, login);
            return false;
        }
        return deleteStudent(s);
    }

    @Override
    public boolean deleteStudent(Student student) {
        return remove(student);
    }
}