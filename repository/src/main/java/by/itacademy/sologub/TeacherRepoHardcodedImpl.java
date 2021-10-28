package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

@Slf4j
public class TeacherRepoHardcodedImpl implements TeacherRepo {
    public static TeacherRepoHardcodedImpl instance;
    static int CURRENT_MAX_TEACHER_ID = 10;
    private final CredentialRepo credentialRepo;
    private final Map<Credential, Teacher> teachers;

    private TeacherRepoHardcodedImpl(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
        teachers = new HashMap<>();
    }

    public static TeacherRepoHardcodedImpl getInstance(CredentialRepo credentialRepo) {
        if (instance == null) {
            synchronized (CredentialRepoHardcodeImpl.class) {
                if (instance == null) {
                    instance = new TeacherRepoHardcodedImpl(credentialRepo);
                }
            }
        }
        return instance;
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Возвращаем {}", TEACHER_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else {
            log.info("Объект учётных данных есть в базе. Пытаемся достать Teacher");
            return teachers.get(cr);
        }
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Возвращаем {}", TEACHER_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            log.info("Объект учётных данных есть но пароль неверен. Возвращаем {}", TEACHER_PASSWORD_WRONG);
            return TEACHER_PASSWORD_WRONG;
        } else {
            log.info("Объект учётных данных есть в базе. Пытаемся достать Teacher");
            return teachers.get(cr);
        }
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        Credential cr = teacher.getCredential();
        log.debug("Пытаемся проверить учетные данные {} обьекта Teacher в репозитории", cr);
        boolean result = credentialRepo.putCredentialIfNotExists(cr.getLogin(), cr.getPassword());
        if (result) {
            Credential teacherCr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(cr.getLogin());
            log.debug("Учетные данные добавлены, пытаемся положить обьект {} в репозиторй", teacherCr);
            if (isExistsAndPasswordRight(teacherCr)) {
                teacher.setId(CURRENT_MAX_TEACHER_ID++);
                teacher.setCredential(teacherCr);
                teachers.put(teacherCr, teacher);
                log.debug("Teacher {} добавлен в репозиторий", teacher);
                return true;
            } else {
                log.debug("Teacher не добавлен в репозиторий. Логин или пароль не совпали");
                return false;
            }
        }
        log.debug("Учетные данные уже существуют Teacher не будет добавлен в репозиторий");
        return false;
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newT) {
        Credential newCred = credentialRepo.getCredentialIfExistsOrGetSpecialValue(newT.getCredential().getLogin());
        Teacher oldT = teachers.get(newCred);
        log.debug("Пытаемся менять параметры Teacher и Credential на новые");

        if (oldT != null && TEACHER_NOT_EXISTS != oldT) {
            log.debug("Меняем параметры Teacher и Credential на новые");
            oldT.setFirstname(newT.getFirstname());
            oldT.setLastname(newT.getLastname());
            oldT.setPatronymic(newT.getPatronymic());
            oldT.setDateOfBirth(newT.getDateOfBirth());

            Credential oldCred = oldT.getCredential();
            oldCred.setLogin(newCred.getLogin());
            oldCred.setPassword(newCred.getPassword());
            return true;
        } else {
            log.debug("Нельзя менять параметры Teacher или Credential если их не существует в репозитории");
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Нечего удалять");
            return false;
        } else {
            teachers.remove(cr);
            credentialRepo.deleteCredentialIfExists(login);
            log.info("Объекты Teacher и Credentials удалены из репозиториев");
            return true;
        }
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return deleteTeacher(teacher.getCredential().getLogin());
    }

    @Override
    public List<Teacher> getTeachersList() {
        return new ArrayList<>(teachers.values());
    }
}