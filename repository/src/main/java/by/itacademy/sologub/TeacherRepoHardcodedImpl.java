package by.itacademy.sologub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

public class TeacherRepoHardcodedImpl implements TeacherRepo {
    static int CURRENT_MAX_TEACHER_ID = 10;
    private static final Logger Log = LoggerFactory.getLogger(TeacherRepoHardcodedImpl.class);
    private final CredentialRepo credentialRepo;
    private final Map<Credential, Teacher> teachers;

    public TeacherRepoHardcodedImpl(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
        teachers = new HashMap<>();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        Log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            Log.info("Объекта учётных данных нет в базе. Возвращаем {}", TEACHER_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else {
            Log.info("Объект учётных данных есть в базе. Пытаемся достать Teacher");
            return teachers.get(cr);
        }
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        Log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            Log.info("Объекта учётных данных нет в базе. Возвращаем {}", TEACHER_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            Log.info("Объект учётных данных есть но пароль неверен. Возвращаем {}", TEACHER_PASSWORD_WRONG);
            return TEACHER_PASSWORD_WRONG;
        } else {
            Log.info("Объект учётных данных есть в базе. Пытаемся достать Teacher");
            return teachers.get(cr);
        }
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        Credential cr = teacher.getCredential();
        Log.debug("Пытаемся проверить учетные данные {} обьекта Teacher в репозитории", cr);
        boolean result = credentialRepo.putCredentialIfNotExists(cr.getLogin(), cr.getPassword());
        if (result) {
            Credential teacherCr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(cr.getLogin());
            Log.debug("Учетные данные добавлены, пытаемся положить обьект {} в репозиторй", teacherCr);
            if (isExistsAndPasswordRight(teacherCr)) {
                teacher.setId(CURRENT_MAX_TEACHER_ID++);
                teacher.setCredential(teacherCr);
                teachers.put(teacherCr, teacher);
                Log.debug("Teacher {} добавлен в репозиторий", teacher);
                return true;
            } else {
                Log.debug("Teacher не добавлен в репозиторий. Логин или пароль не совпали");
                return false;
            }
        }
        Log.debug("Учетные данные уже существуют Teacher не будет добавлен в репозиторий");
        return false;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newT) {
        Credential newCred = credentialRepo.getCredentialIfExistsOrGetSpecialValue(newT.getCredential().getLogin());
        Teacher oldT = teachers.get(newCred);
        Log.debug("Пытаемся менять параметры Teacher и Credential на новые");

        if (oldT != null && TEACHER_NOT_EXISTS != oldT) {
            Log.debug("Меняем параметры Teacher и Credential на новые");
            oldT.setFirstname(newT.getFirstname());
            oldT.setLastname(newT.getLastname());
            oldT.setPatronymic(newT.getPatronymic());
            oldT.setDateOfBirth(newT.getDateOfBirth());

            Credential oldCred = oldT.getCredential();
            oldCred.setLogin(newCred.getLogin());
            oldCred.setPassword(newCred.getPassword());
            return true;
        } else {
            Log.debug("Нельзя менять параметры Teacher или Credential если их не существует в репозитории");
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        Log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            Log.info("Объекта учётных данных нет в базе. Нечего удалять");
            return false;
        } else {
            teachers.remove(cr);
            credentialRepo.removeCredentialIfExists(login);
            Log.info("Объекты Teacher и Credentials удалены из репозиториев");
            return true;
        }
    }

    @Override
    public List<Teacher> getTeachersList() {
        return new ArrayList<>(teachers.values());
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}