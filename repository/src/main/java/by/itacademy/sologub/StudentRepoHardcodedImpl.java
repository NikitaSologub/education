package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itacademy.sologub.constants.ConstantObject.*;

@Slf4j
public class StudentRepoHardcodedImpl implements StudentRepo {
    static int CURRENT_MAX_STUDENT_ID = 6789;
    private static StudentRepoHardcodedImpl instance;
    private final CredentialRepo credentialRepo;
    private final Map<Integer, Student> students;

    private StudentRepoHardcodedImpl(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
        students = new HashMap<>();
    }

    public static StudentRepoHardcodedImpl getInstance(CredentialRepo credentialRepo) {
        if (instance == null) {
            synchronized (StudentRepoHardcodedImpl.class) {
                if (instance == null) {
                    instance = new StudentRepoHardcodedImpl(credentialRepo);
                }
            }
        }
        return instance;
    }

    @Override
    public List<Student> getStudentsList() {
        return new ArrayList<>(students.values());
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return STUDENT_NOT_EXISTS;
        } else {
            log.info("Логин существует, патаемся вернуть студента");
            return students.values().stream()
                    .filter(s -> s.getCredential().getLogin().equals(login))
                    .findAny().orElse(STUDENT_NOT_EXISTS);
        }
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return STUDENT_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            log.info("Прароль не верен, возвращаем специальный обьект {}", STUDENT_PASSWORD_WRONG);
            return STUDENT_PASSWORD_WRONG;
        } else {
            log.info("Логин и пароль соответствуют, патаемся вернуть студента");
            return students.values().stream()
                    .filter(s -> s.getCredential().getLogin().equals(login) &&
                            s.getCredential().getPassword().equals(password))
                    .findAny().orElse(STUDENT_NOT_EXISTS);
        }
    }

    @Override
    public boolean putStudentIfNotExists(Student student) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(student.getCredential().getLogin());
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("логина не существует, значит можно добавить нового студента");
            String login = student.getCredential().getLogin();
            String password = student.getCredential().getPassword();

            credentialRepo.putCredentialIfNotExists(login, password);
            cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);

            if (isExistsAndPasswordRight(cr)) {
                student.setId(CURRENT_MAX_STUDENT_ID++);
                student.setCredential(cr);
                students.put(student.getId(), student);
                log.info("Удалось положить Credential, добавяем нового студента {}", student);
                return true;
            } else {
                log.info("Не удалось положить Credential, нельзя добавить нового студента");
            }
        }
        return false;
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newS) {
        Student oldS = students.get(newS.getId());
        log.debug("Пытаемся менять параметры Student и Credential на новые");

        if (oldS != null && STUDENT_NOT_EXISTS != oldS) {
            log.debug("Меняем параметры Student и Credential на новые");

            oldS.setFirstname(newS.getFirstname());
            oldS.setLastname(newS.getLastname());
            oldS.setPatronymic(newS.getPatronymic());
            oldS.setDateOfBirth(newS.getDateOfBirth());
            boolean isChanged = credentialRepo.changeCredentialIfExists(newS.getCredential().getLogin(), newS.getCredential().getPassword());
            if (isChanged) {
                log.info("Учётная запись изменена, можно менять параметры студента");
            } else {
                log.info("Учётная запись не изменена, но параметры студента будут изменены");
            }
            return true;
        } else {
            log.debug("Нельзя менять параметры Student или Credential если их не существует в репозитории");
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся проверить учетные данные обьекта Student {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Нечего удалять");
        } else {
            Student s = students.values().stream()
                    .filter(student -> student.getCredential().getLogin().equals(cr.getLogin()))
                    .findAny().orElse(STUDENT_NOT_EXISTS);
            if (STUDENT_NOT_EXISTS != s) {
                s.setCredential(null);// help GC
                students.remove(s.getId());
                credentialRepo.deleteCredentialIfExists(login);
                log.info("Объекты Student и Credentials удалены из репозиториев");
                return true;
            } else {
                log.info("Объекта студентов нет в базе. Нечего удалять");
            }
        }
        return false;
    }

    @Override
    public boolean deleteStudent(Student student) {
        return deleteStudent(student.getCredential().getLogin());
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}