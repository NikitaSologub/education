package by.itacademy.sologub;

import java.util.HashMap;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

public class TeacherRepoHardcodedImpl implements TeacherRepo {
    static int CURRENT_MAX_TEACHER_ID = 10;
    private final CredentialRepo credentialRepo;
    private final Map<Credential, Teacher> teachers;

    public TeacherRepoHardcodedImpl(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
        teachers = new HashMap<>();
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            return TEACHER_NOT_EXISTS;
        } else {
            return teachers.get(cr);
        }
    }

    @Override
    public Teacher getTeacherIfExistsOrGetSpecialValue(String login, String password) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);
        if (LOGIN_NOT_EXISTS == cr) {
            return TEACHER_NOT_EXISTS;
        } else if (PASSWORD_WRONG == cr) {
            return TEACHER_PASSWORD_WRONG;
        } else {
            return teachers.get(cr);
        }
    }

    @Override
    public boolean putTeacherIfNotExists(Teacher teacher) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(teacher.getCredential().getLogin());
        if (LOGIN_NOT_EXISTS == cr) {
            String login = teacher.getCredential().getLogin();
            String password = teacher.getCredential().getPassword();

            credentialRepo.putCredentialIfNotExists(login, password);
            Credential teacherCr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login, password);

            if(isExistsAndPasswordRight(teacherCr)){
                teacher.setId(CURRENT_MAX_TEACHER_ID++);
                teacher.setCredential(teacherCr);
                teachers.put(teacherCr,teacher);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isExistsAndPasswordRight(Credential cr){
        return (LOGIN_NOT_EXISTS != cr && PASSWORD_WRONG != cr);
    }
}