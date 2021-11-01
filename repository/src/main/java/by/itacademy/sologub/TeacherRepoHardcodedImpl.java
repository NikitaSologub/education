package by.itacademy.sologub;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

@Slf4j
public class TeacherRepoHardcodedImpl implements TeacherRepo {
    static int CURRENT_MAX_TEACHER_ID = 19051;
    private static TeacherRepoHardcodedImpl instance;
    private final CredentialRepo credentialRepo;
    private final Map<Integer, Teacher> teachers;

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
    public List<Teacher> getTeachersList() {
        return new ArrayList<>(teachers.values());
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else {
            log.info("Логин существует, патаемся вернуть учителя");
            return teachers.values().stream()
                    .filter(s -> s.getCredential().getLogin().equals(login))
                    .findAny().orElse(TEACHER_NOT_EXISTS);
        }
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Логина не существует, возвращаем специальный обьект {}", LOGIN_NOT_EXISTS);
            return TEACHER_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            log.info("Прароль не верен, возвращаем специальный обьект {}", TEACHER_PASSWORD_WRONG);
            return TEACHER_PASSWORD_WRONG;
        } else {
            log.info("Логин и пароль соответствуют, патаемся вернуть учителя");
            return teachers.values().stream()
                    .filter(s -> s.getCredential().getLogin().equals(login) &&
                            s.getCredential().getPassword().equals(password))
                    .findAny().orElse(TEACHER_NOT_EXISTS);
        }
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(teacher.getCredential().getLogin());
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("логина не существует, значит можно добавить нового учителя");
            String login = teacher.getCredential().getLogin();
            String password = teacher.getCredential().getPassword();

            credentialRepo.putCredentialIfNotExists(login, password);
            cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);

            if (isExistsAndPasswordRight(cr)) {
                teacher.setId(CURRENT_MAX_TEACHER_ID++);
                teacher.setCredential(cr);
                teachers.put(teacher.getId(), teacher);
                log.info("Удалось положить Credential, добавяем нового учителя {}", teacher);
                return true;
            } else {
                log.info("Не удалось положить Credential, нельзя добавить нового учителя");
            }
        }
        return false;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newT) {
        Teacher oldT = teachers.get(newT.getId());
        log.debug("Пытаемся менять параметры Teacher и Credential на новые");

        if (oldT != null && TEACHER_NOT_EXISTS != oldT) {
            log.debug("Меняем параметры Teacher и Credential на новые");

            oldT.setFirstname(newT.getFirstname());
            oldT.setLastname(newT.getLastname());
            oldT.setPatronymic(newT.getPatronymic());
            oldT.setDateOfBirth(newT.getDateOfBirth());
            boolean isChanged = credentialRepo.changeCredentialIfExists(newT.getCredential().getLogin(), newT.getCredential().getPassword());
            if (isChanged) {
                log.info("Учётная запись изменена, можно менять параметры учителя");
            } else {
                log.info("Учётная запись не изменена, но параметры учителя будут изменены");
            }
            return true;
        } else {
            log.debug("Нельзя менять параметры Teacher или Credential если их не существует в репозитории");
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        log.debug("Пытаемся проверить учетные данные обьекта Teacher {} в репозитории", cr);
        if (LOGIN_NOT_EXISTS == cr) {
            log.info("Объекта учётных данных нет в базе. Нечего удалять");
        } else {
            Teacher t = teachers.values().stream()
                    .filter(student -> student.getCredential().getLogin().equals(cr.getLogin()))
                    .findAny().orElse(TEACHER_NOT_EXISTS);
            if (TEACHER_NOT_EXISTS != t) {
                t.setCredential(null);// help GC
                teachers.remove(t.getId());
                credentialRepo.deleteCredentialIfExists(login);
                log.info("Объекты Teacher и Credentials удалены из репозиториев");
                return true;
            } else {
                log.info("Объекта учителей нет в базе. Нечего удалять");
            }
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(Teacher teacher) {
        return deleteTeacher(teacher.getCredential().getLogin());
    }

    private boolean isExistsAndPasswordRight(Credential cr) {
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}