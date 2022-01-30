package by.itacademy.sologub.spring_orm;

import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.model.Teacher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Slf4j
@Transactional
@Repository
public class TeacherRepoSpringOrmImpl extends AbstractSpringOrm<Teacher> implements TeacherRepo {
    @Autowired
    protected TeacherRepoSpringOrmImpl() {
        super(Teacher.class, TEACHER_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Teacher> getTeachersSet() {
        List<Teacher> teachers = findAll();
        log.info("возвращаем {}", teachers);
        return new HashSet<>(teachers);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return getByNamedQueryIntArgument("getTeacherById", id, ID);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        return getByNamedQueryStringArgument("getTeacherByLogin", login, LOGIN);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Teacher t = getByNamedQueryStringArgument("getTeacherByLogin", login, LOGIN);
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
        return inputIfNotExists(teacher);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        Teacher old = getByNamedQueryIntArgument("getTeacherById", newTeacher.getId(), ID);
        Set<Salary> salaries = old.getSalaries();
        newTeacher.setSalaries(salaries);
        return updateIfExists(newTeacher);
    }

    @Override
    public boolean deleteTeacher(String login) {
        Teacher teacher = getByNamedQueryStringArgument("getTeacherByLogin", login, LOGIN);
        if (TEACHER_NOT_EXISTS == teacher) {
            log.info("учителя с login={} нет в БД", login);
            return false;
        }
        excludeTeacherFromGroups(teacher.getId());
        return removeIfExists(teacher);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        if (TEACHER_NOT_EXISTS == teacher) {
            log.info("учителя {} нет в БД", teacher);
            return false;
        }
        excludeTeacherFromGroups(teacher.getId());
        return removeIfExists(teacher);
    }

    private void excludeTeacherFromGroups(int id) {
        int groupNum = em
                .createNamedQuery("setNullWhereTeacherId")
                .setParameter(ID, id)
                .executeUpdate();
        log.debug("Перед удалением учителя с id={} сняли с должности в {} группах", id, groupNum);
    }
}