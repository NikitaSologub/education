package by.itacademy.sologub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Credential cr = teacher.getCredential();
        boolean result = credentialRepo.putCredentialIfNotExists(cr.getLogin(), cr.getPassword());
        if (result) {
            Credential teacherCr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(cr.getLogin());
            if (isExistsAndPasswordRight(teacherCr)) {
                teacher.setId(CURRENT_MAX_TEACHER_ID++);
                teacher.setCredential(teacherCr);
                teachers.put(teacherCr, teacher);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean changeTeachersParametersIfExists(Teacher newT){
        Credential newCred = credentialRepo.getCredentialIfExistsOrGetSpecialValue(newT.getCredential().getLogin());
        System.out.println("newCred " + newCred);
        Teacher oldT = teachers.get(newCred);
        System.out.println("oldT " + oldT);
        if(oldT != null && TEACHER_NOT_EXISTS != oldT){
            oldT.setFirstname(newT.getFirstname());
            oldT.setLastname(newT.getLastname());
            oldT.setPatronymic(newT.getPatronymic());
            oldT.setDateOfBirth(newT.getDateOfBirth());

            Credential oldCred = oldT.getCredential();
            oldCred.setLogin(newCred.getLogin());
            oldCred.setPassword(newCred.getPassword());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteTeacher(String login) {
        Credential cr = credentialRepo.getCredentialIfExistsOrGetSpecialValue(login);
        if (LOGIN_NOT_EXISTS == cr) {
            return false;
        } else {
            teachers.remove(cr);
            credentialRepo.removeCredentialIfExists(login);
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