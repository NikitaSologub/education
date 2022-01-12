package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Slf4j
@Repository
public class TeacherRepoHibernateImpl extends AbstractCrudRepoJpa<Teacher> implements TeacherRepo {
    @Autowired
    public TeacherRepoHibernateImpl(SessionFactory sf) {
        super(sf, Teacher.class);
    }

    @Override
    protected Teacher getEmptyObj() {
        return TEACHER_NOT_EXISTS;
    }

    @Override
    public Set<Teacher> getTeachersSet() {
        return new HashSet<>(getAll());
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        Teacher t = getByNamedQueryIntArgument("getTeacherById", id, ID);
        return getObjOrSpecialEmpty(t, String.valueOf(id), ID);
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Teacher t = getByNamedQueryStringArgument("getTeacherByLogin", login, LOGIN);
        return getObjOrSpecialEmpty(t, login, LOGIN);
    }

    private Teacher getObjOrSpecialEmpty(Teacher t, String value, String columnName) {
        if (t == null) {
            log.debug("Не получилось получить обьект Teacher по {}={}", columnName, value);
            return TEACHER_NOT_EXISTS;
        }
        return t;
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Teacher t = getTeacherIfExistsOrGetSpecialValue(login);
        if (TEACHER_NOT_EXISTS != t) {
            if (t.getCredential().getPassword().equals(password)) {
                return t;
            } else {
                log.debug("Не получилось получить обьект Teacher по {}={}. Пароль не верен", LOGIN, login);
                return TEACHER_PASSWORD_WRONG;
            }
        }
        return t;
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        return input(teacher);
    }

    @Override //todo  (вернёт true даже если нет изменений)
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        Set<Salary> salaries = getTeacherIfExistsOrGetSpecialValue(newTeacher.getId()).getSalaries();
        newTeacher.setSalaries(salaries);
        return change(newTeacher);
    }

    @Override
    public boolean deleteTeacher(String login) {
        Teacher t = getTeacherIfExistsOrGetSpecialValue(login);
        if (TEACHER_NOT_EXISTS == t) {
            log.debug("Не получилось удалить обьект Teacher по {}={}", LOGIN, login);
            return false;
        }
        return deleteTeacher(t);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return remove(teacher);
    }
}