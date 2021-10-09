package by.itacademy.sologub.constants;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.Credential;
import by.itacademy.sologub.role.Role;

import java.time.LocalDate;
import java.time.Month;

public class Constants {
    public static final Credential LOGIN_NOT_EXISTS;
    public static final Credential LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG;

    public static final Credential ADMIN_CREDENTIAL;
    public static final Admin ADMIN_USER;

    static {
        ADMIN_CREDENTIAL = new Credential();
        ADMIN_CREDENTIAL.setId(0);
        ADMIN_CREDENTIAL.setLogin("ADMIN");
        ADMIN_CREDENTIAL.setPassword("234");

        LOGIN_NOT_EXISTS = new Credential();
        LOGIN_NOT_EXISTS.setId(-1);
        LOGIN_NOT_EXISTS.setLogin("LOGIN_NOT_EXISTS");
        LOGIN_NOT_EXISTS.setPassword("LOGIN_NOT_EXISTS");

        LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG = new Credential();
        LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG.setId(-2);
        LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG.setLogin("LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG");
        LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG.setPassword("LOGIN_NOT_EXISTS_OR_PASSWORD_WRONG");

        ADMIN_USER = new Admin();
        ADMIN_USER.setId(ADMIN_CREDENTIAL.getId());
        ADMIN_USER.setCredential(ADMIN_CREDENTIAL);
        ADMIN_USER.setFirstname("Никита");
        ADMIN_USER.setLastname("Сологуб");
        ADMIN_USER.setPatronymic("Олегович");
        ADMIN_USER.setDateOfBirth(LocalDate.of(1992, Month.APRIL,23));
        ADMIN_USER.setRole(Role.ADMIN);
    }
}
