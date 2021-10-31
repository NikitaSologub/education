package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

@Slf4j
public class StudentRepoHardcodedImpl implements StudentRepo {
    static int CURRENT_MAX_STUDENT_ID = 100;
    private static StudentRepoHardcodedImpl instance;
    private final CredentialRepo credentialRepo;
    private final Map<Credential, Student> students;

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
            return STUDENT_NOT_EXISTS;
        } else {
            return students.get(cr);
        }
    }

    @Override
    public Student getStudentIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            return STUDENT_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            return STUDENT_PASSWORD_WRONG;
        } else {
            return students.get(cr);
        }
    }

    @Override
    public boolean putStudentIfNotExists(Student student) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(student.getCredential().getLogin());
        if (LOGIN_NOT_EXISTS == cr) {
            String login = student.getCredential().getLogin();
            String password = student.getCredential().getPassword();

            credentialRepo.putCredentialIfNotExists(login, password);
            Credential studentCr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);

            if (isExistsAndPasswordRight(studentCr)) {
                student.setId(CURRENT_MAX_STUDENT_ID++);
                student.setCredential(studentCr);
                students.put(studentCr, student);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean changeStudentParametersIfExists(Student newS) {
        Credential oldCred = credentialRepo.getCredentialIfExistsOrGetSpecialValue(newS.getCredential().getLogin());
        Student oldS = students.get(oldCred);
        log.debug("Пытаемся менять параметры Student и Credential на новые");

        if (oldS != null && STUDENT_NOT_EXISTS != oldS) {
            log.debug("Меняем параметры Student и Credential на новые");

            boolean isChanged = credentialRepo.changeCredentialIfExists(newS.getCredential().getLogin(), newS.getCredential().getPassword());
            if (isChanged) {
                oldS.setFirstname(newS.getFirstname());
                oldS.setLastname(newS.getLastname());
                oldS.setPatronymic(newS.getPatronymic());
                oldS.setDateOfBirth(newS.getDateOfBirth());
                log.info("Учётная запись изменена, можно менять параметры студента");
                return true;
            } else {
                log.info("Учётная запись не изменена, параметры студента тоже не будут изменены");
            }
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
            return false;
        } else {
            students.remove(cr);
            credentialRepo.deleteCredentialIfExists(login);
            log.info("Объекты Student и Credentials удалены из репозиториев");
            return true;
        }
    }

    @Override
    public boolean deleteStudent(Student student) {
        return deleteStudent(student.getCredential().getLogin());
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}