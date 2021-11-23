package by.itacademy.sologub.constants;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Group;
import by.itacademy.sologub.Mark;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.Teacher;

import java.time.LocalDate;

public final class ConstantObject {
    private static final String NO_TITLE = "NO_TITLE";
    private static final String NO_DESCRIPTION = "NO_DESCRIPTION";
    private static final String LOGIN_IS_NOT_EXISTS = "LOGIN_IS_NOT_EXISTS";
    private static final String ID_IS_NOT_EXISTS = "ID_IS_NOT_EXISTS";
    private static final String PASSWORD_IS_WRONG = "PASSWORD_IS_WRONG";
    private static final String SUBJECT_IS_NOT_EXISTS = "SUBJECT_IS_NOT_EXISTS";

    public static final Credential CREDENTIAL_NOT_EXISTS;
    public static final Credential LOGIN_NOT_EXISTS;
    public static final Credential PASSWORD_WRONG;

    public static final Admin ADMIN_NOT_EXISTS;
    public static final Admin ADMIN_PASSWORD_WRONG;

    public static final Teacher TEACHER_NOT_EXISTS;
    public static final Teacher TEACHER_PASSWORD_WRONG;

    public static final Student STUDENT_NOT_EXISTS;
    public static final Student STUDENT_PASSWORD_WRONG;

    public static final Salary SALARY_NOT_EXISTS;
    public static final Mark MARK_NOT_EXISTS;
    public static final Subject SUBJECT_NOT_EXISTS;
    public static final Group GROUP_NOT_EXISTS;

    static {
        CREDENTIAL_NOT_EXISTS = new Credential();
        CREDENTIAL_NOT_EXISTS.setId(-1);
        CREDENTIAL_NOT_EXISTS.setLogin(ID_IS_NOT_EXISTS);
        CREDENTIAL_NOT_EXISTS.setPassword(ID_IS_NOT_EXISTS);

        LOGIN_NOT_EXISTS = new Credential();
        LOGIN_NOT_EXISTS.setId(-1);
        LOGIN_NOT_EXISTS.setLogin(LOGIN_IS_NOT_EXISTS);
        LOGIN_NOT_EXISTS.setPassword(LOGIN_IS_NOT_EXISTS);

        PASSWORD_WRONG = new Credential();
        PASSWORD_WRONG.setId(-2);
        PASSWORD_WRONG.setLogin(PASSWORD_IS_WRONG);
        PASSWORD_WRONG.setPassword(PASSWORD_IS_WRONG);

        TEACHER_NOT_EXISTS = new Teacher();
        TEACHER_NOT_EXISTS.setId(-4);
        TEACHER_NOT_EXISTS.setCredential(LOGIN_NOT_EXISTS);

        TEACHER_PASSWORD_WRONG = new Teacher();
        TEACHER_PASSWORD_WRONG.setId(-5);
        TEACHER_PASSWORD_WRONG.setCredential(PASSWORD_WRONG);

        STUDENT_NOT_EXISTS = new Student();
        STUDENT_NOT_EXISTS.setId(-6);
        STUDENT_NOT_EXISTS.setCredential(LOGIN_NOT_EXISTS);

        STUDENT_PASSWORD_WRONG = new Student();
        STUDENT_PASSWORD_WRONG.setId(-7);
        STUDENT_PASSWORD_WRONG.setCredential(PASSWORD_WRONG);

        ADMIN_NOT_EXISTS = new Admin();
        ADMIN_NOT_EXISTS.setId(-6);
        ADMIN_NOT_EXISTS.setCredential(LOGIN_NOT_EXISTS);

        ADMIN_PASSWORD_WRONG = new Admin();
        ADMIN_PASSWORD_WRONG.setId(-7);
        ADMIN_PASSWORD_WRONG.setCredential(PASSWORD_WRONG);

        SALARY_NOT_EXISTS = new Salary();
        SALARY_NOT_EXISTS.setId(-1);
        SALARY_NOT_EXISTS.setCoins(0);
        SALARY_NOT_EXISTS.setDate(LocalDate.now());

        MARK_NOT_EXISTS = new Mark();
        MARK_NOT_EXISTS.setId(-1);
        MARK_NOT_EXISTS.setPoint(-100);

        SUBJECT_NOT_EXISTS = new Subject();
        SUBJECT_NOT_EXISTS.setId(-1);
        SUBJECT_NOT_EXISTS.setTitle(SUBJECT_IS_NOT_EXISTS);

        GROUP_NOT_EXISTS = new Group();
        GROUP_NOT_EXISTS.setId(-1);
        GROUP_NOT_EXISTS.setTeacher(TEACHER_NOT_EXISTS);
        GROUP_NOT_EXISTS.setTitle(NO_TITLE);
        GROUP_NOT_EXISTS.setDescription(NO_DESCRIPTION);
    }
}