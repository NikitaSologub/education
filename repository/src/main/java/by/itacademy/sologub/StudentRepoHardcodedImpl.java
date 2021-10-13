package by.itacademy.sologub;

import java.util.HashMap;
import java.util.Map;

import static by.itacademy.sologub.constants.Constants.*;

public class StudentRepoHardcodedImpl implements StudentRepo{
    static int CURRENT_MAX_STUDENT_ID = 100;
    private final CredentialRepo credentialRepo;
    private final Map<Credential, Student> students;

    public StudentRepoHardcodedImpl(CredentialRepo credentialRepo) {
        this.credentialRepo = credentialRepo;
        students = new HashMap<>();
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

            if(isExistsAndPasswordRight(studentCr)){
                student.setId(CURRENT_MAX_STUDENT_ID++);
                student.setCredential(studentCr);
                students.put(studentCr,student);
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