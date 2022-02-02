package by.itacademy.sologub.spring_data;

import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.spring_data.crud.CredentialDataAccess;
import by.itacademy.sologub.spring_data.crud.GroupDataAccess;
import by.itacademy.sologub.spring_data.crud.StudentDataAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.ConstantObject.CREDENTIAL_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;

@Slf4j
@Transactional
@Repository
public class StudentRepoSpringDataImpl implements StudentRepo {
    private final StudentDataAccess studentDao;
    private final CredentialDataAccess credentialDao;
    private final GroupDataAccess groupDao;

    @Autowired
    public StudentRepoSpringDataImpl(CredentialDataAccess credentialDao, StudentDataAccess studentDao, GroupDataAccess groupDao) {
        this.studentDao = studentDao;
        this.credentialDao = credentialDao;
        this.groupDao = groupDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsSet() {
        return studentDao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<Student> getStudentsByGroupId(int groupId) {
        Group group = groupDao.findById(groupId).orElse(GROUP_NOT_EXISTS);
        if (GROUP_NOT_EXISTS != group) {
            return group.getStudents();
        }
        return new HashSet<>();
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(int id) {
        return studentDao.findById(id).orElse(STUDENT_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        Credential cred = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        if (CREDENTIAL_NOT_EXISTS == cred) {
            return STUDENT_NOT_EXISTS;
        }
        return studentDao.findByCredentialId(cred.getId()).orElse(STUDENT_NOT_EXISTS);
    }

    @Override
    @Transactional(readOnly = true)
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Student student = getStudentIfExistsOrGetSpecialValue(login);
        if (password.equals(student.getCredential().getPassword())) {
            return student;
        }
        return STUDENT_PASSWORD_WRONG;
    }

    @Override
    public boolean putStudentIfNotExists(Student student) {
        log.debug("добавляем нового студента в БД {}", student);
        Student studentInDb = studentDao.save(student);
        return studentInDb.getId() != 0;
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newStudent) {
        Student oldStudentInDb = studentDao.findById(newStudent.getId()).orElse(STUDENT_NOT_EXISTS);
        if (STUDENT_NOT_EXISTS != oldStudentInDb) {
            log.debug("корректируем параметры студента на новые {}", newStudent);
            oldStudentInDb.setCredential(newStudent.getCredential());
            oldStudentInDb.setFirstname(newStudent.getFirstname());
            oldStudentInDb.setLastname(newStudent.getLastname());
            oldStudentInDb.setPatronymic(newStudent.getPatronymic());
            oldStudentInDb.setDateOfBirth(newStudent.getDateOfBirth());
            Student newStudentInDb = studentDao.save(oldStudentInDb);
            return newStudentInDb.equals(oldStudentInDb);
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String login) {
        Credential credential = credentialDao.findByLogin(login).orElse(CREDENTIAL_NOT_EXISTS);
        Student student = studentDao.findByCredentialId(credential.getId()).orElse(STUDENT_NOT_EXISTS);
        if (STUDENT_NOT_EXISTS != student) {
            studentDao.deleteById(student.getId());
            return !studentDao.existsById(student.getId());
        }
        return false;
    }

    @Override
    public boolean deleteStudent(Student student) {
        studentDao.delete(student);
        return !studentDao.existsById(student.getId());
    }
}