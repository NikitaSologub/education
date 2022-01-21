package by.itacademy.sologub.hibernate;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
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
        System.out.println("+-+-" + "1/1");
        Teacher t = getTeacherIfExistsOrGetSpecialValue(login);
        if (TEACHER_NOT_EXISTS == t) {
            log.debug("Не получилось удалить обьект Teacher по {}={}", LOGIN, login);
            return false;
        }
        System.out.println("+-+-" + "1/2");
        return deleteTeacher(t);
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        System.out.println("+-+-" + "1");
        if (teacher == null || teacher.getId() <= 0) return false;
        EntityManager manager = getEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        boolean result = false;
        try {
            transaction.begin();
            System.out.println("+-+-" + "2");
            int groupNum = manager.createNamedQuery("setNullWhereTeacherId")
                    .setParameter(ID, teacher.getId())
                    .executeUpdate();
            log.debug("Перед удалением учителя с id={} сняли с должности в {} группах", teacher.getId(), groupNum);
            System.out.println("+-+-" + "3");
            manager.remove(teacher);
            result = !manager.contains(teacher);
            log.debug("Результат удаления{} = {} ", teacher, result);
            System.out.println("+-+-" + "4");
            transaction.commit();
            System.out.println("+-+-" + "5");
        } catch (PersistenceException e) {
            log.error("Не получилось удалить " + teacher + " в БД", e);
            System.out.println("+-+-" + "6");
            transaction.rollback();
        } finally {
            System.out.println("+-+-" + "7");
            manager.close();
        }
        System.out.println("+-+-" + "8");
        return result;
//        return remove(teacher);
    }
}