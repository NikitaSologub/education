package by.itacademy.sologub.constants;

import by.itacademy.sologub.*;

import java.time.LocalDate;
import java.time.Month;

public final class ConstantObject {
    public static final Credential ADMIN_CREDENTIAL;
    public static final Admin ADMIN_USER;

    public static final Credential LOGIN_NOT_EXISTS;
    public static final Credential PASSWORD_WRONG;

    public static final Teacher TEACHER_NOT_EXISTS;
    public static final Teacher TEACHER_PASSWORD_WRONG;

    public static final Student STUDENT_NOT_EXISTS;
    public static final Student STUDENT_PASSWORD_WRONG;

    public static final Salary SALARY_NOT_EXISTS;
    public static final Mark MARK_NOT_EXISTS;
    public static final Subject SUBJECT_NOT_EXISTS;

    static {
        ADMIN_CREDENTIAL = new Credential();
        ADMIN_CREDENTIAL.setId(0);
        ADMIN_CREDENTIAL.setLogin("ADMIN");
        ADMIN_CREDENTIAL.setPassword("234");

        LOGIN_NOT_EXISTS = new Credential();
        LOGIN_NOT_EXISTS.setId(-1);
        LOGIN_NOT_EXISTS.setLogin("LOGIN_NOT_EXISTS");
        LOGIN_NOT_EXISTS.setPassword("LOGIN_NOT_EXISTS");

        PASSWORD_WRONG = new Credential();
        PASSWORD_WRONG.setId(-2);
        PASSWORD_WRONG.setLogin("PASSWORD_WRONG");
        PASSWORD_WRONG.setPassword("PASSWORD_WRONG");

        ADMIN_USER = new Admin();
        ADMIN_USER.setId(ADMIN_CREDENTIAL.getId());
        ADMIN_USER.setCredential(ADMIN_CREDENTIAL);
        ADMIN_USER.setFirstname("Никита");
        ADMIN_USER.setLastname("Сологуб");
        ADMIN_USER.setPatronymic("Олегович");
        ADMIN_USER.setDateOfBirth(LocalDate.of(1992, Month.APRIL, 23));

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

        SALARY_NOT_EXISTS = new Salary();
        SALARY_NOT_EXISTS.setId(-1);
        SALARY_NOT_EXISTS.setCoins(0);
        SALARY_NOT_EXISTS.setDate(LocalDate.now());
        SALARY_NOT_EXISTS.setTeacherId(-100);

        MARK_NOT_EXISTS = new Mark();
        MARK_NOT_EXISTS.setId(-1);
        MARK_NOT_EXISTS.setPoint(-100);
        MARK_NOT_EXISTS.setStudentId(-100);

        SUBJECT_NOT_EXISTS = new Subject();
        SUBJECT_NOT_EXISTS.setId(-1);
        SUBJECT_NOT_EXISTS.setTitle("SUBJECT_NOT_EXISTS");
    }
}