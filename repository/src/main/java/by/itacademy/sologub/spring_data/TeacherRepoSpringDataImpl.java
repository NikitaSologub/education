package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.spring_data.crud.CredentialDataAccess;
import by.itacademy.sologub.spring_data.crud.TeacherDataAccess;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Repository
public class TeacherRepoSpringDataImpl implements TeacherRepo {
    private final TeacherDataAccess teacherDao;
    private final CredentialDataAccess credentialDao;

    @Override
    @Transactional(readOnly = true)
    public Set<Teacher> getTeachersSet() {
        return teacherDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(int id) {
        return teacherDao.findById(id).orElse(TEACHER_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Credential cred = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS == cred) {
            return TEACHER_NOT_EXISTS;
        }
        return teacherDao.findByCredentialId(cred.getId()).orElse(TEACHER_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Teacher teacher = getTeacherIfExistsOrGetSpecialValue(login);
        if (password.equals(teacher.getCredential().getPassword())) {
            return teacher;
        }
        return TEACHER_PASSWORD_WRONG;
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        log.debug("добавляем нового учителя в БД {}", teacher);
        Teacher teacherInDb = teacherDao.save(teacher);
        return teacherInDb.getId() != 0;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newTeacher) {
        Teacher oldTeacherInDb = teacherDao.findById(newTeacher.getId()).orElse(TEACHER_NOT_EXISTS);
        if (TEACHER_NOT_EXISTS != oldTeacherInDb) {
            log.debug("корректируем параметры учителя на новые {}", newTeacher);
            oldTeacherInDb.setCredential(newTeacher.getCredential());
            oldTeacherInDb.setFirstname(newTeacher.getFirstname());
            oldTeacherInDb.setLastname(newTeacher.getLastname());
            oldTeacherInDb.setPatronymic(newTeacher.getPatronymic());
            oldTeacherInDb.setDateOfBirth(newTeacher.getDateOfBirth());
            Teacher newTeacherInDb = teacherDao.save(oldTeacherInDb);
            return newTeacherInDb.equals(oldTeacherInDb);
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(String login) {
        Credential credential = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        Teacher teacher = teacherDao.findByCredentialId(credential.getId()).orElse(TEACHER_NOT_EXISTS);
        if (TEACHER_NOT_EXISTS != teacher) {
            teacherDao.deleteById(teacher.getId());
            return !teacherDao.existsById(teacher.getId());
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        teacherDao.delete(teacher);
        return !teacherDao.existsById(teacher.getId());
    }
}