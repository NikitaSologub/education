package by.itacademy.sologub.controllers;

import by.itacademy.sologub.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.*;

@WebServlet(LOGIN_CONTROLLER)
@Slf4j
public class LoginController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        log.info("Попытка входа через недопустимый протокол");
        forwardError(LOGIN_PAGE, "Вам нельзя переходить по URL /LoginController", req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String login = req.getParameter(LOGIN);
        String password = req.getParameter(PASSWORD);

        if (login != null) {
            login = login.trim();
        }
        if (password != null) {
            password = password.trim();
        }
        log.info("Пользователь {} пытается войти в систему. Пароль {}", login, password);

        if (!checkAdminLogIn(login, password, req, res)) {
            if (!checkTeacherLogIn(login, password, req, res)) {
                if (!checkStudentLogIn(login, password, req, res)) {
                    log.info("логина {} нет в системе в доступе отказано.", login);
                    forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
                }
            }
        }
    }

    boolean checkAdminLogIn(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String adminLogin = ADMIN_CREDENTIAL.getLogin();
        String adminPassword = ADMIN_CREDENTIAL.getPassword();

        if (adminLogin.equals(login)) {
            if (adminPassword.equals(password)) {
                createSessionAndSetAttribute(ADMIN_USER, req);
                log.info("Логин и пароль администратора совпали. Админ входит в систему. Форвард на ADMIN_FRONT_PAGE");
                forward(ADMIN_FRONT_PAGE, "добро пожаловать ADMIN", req, res);
                return true;
            } else {
                forwardError(LOGIN_PAGE, "Введён неверный пароль.", req, res);
                log.info("Логин совпал а пароль не верен. АДМИН- в доступе отказано. Форвард на LOGIN_PAGE");
            }
        }
        return false;
    }

    void createSessionAndSetAttribute(User user, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_ENTITY, user);
        log.info("пользователь {} положен в сессию.", user);
    }

    boolean checkTeacherLogIn(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Teacher teacher = repo.getTeacherIfExistsOrGetSpecialValue(login, password);

        if (teacher != null && TEACHER_NOT_EXISTS != teacher) {
            if (TEACHER_PASSWORD_WRONG != teacher) {
                createSessionAndSetAttribute(teacher, req);
                log.info("Логин и пароль учителя совпали. Учитель входит в систему. Форвард на TEACHER_FRONT_PAGE");
                forward(TEACHER_FRONT_PAGE, "добро пожаловать TEACHER", req, res);
                return true;
            } else {
                forwardError(LOGIN_PAGE, "Введён неверный пароль.", req, res);
                log.info("Логин совпал а пароль не верен. TEACHER- в доступе отказано. Форвард на LOGIN_PAGE");
            }
        }
        return false;
    }

    boolean checkStudentLogIn(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        if (repo == null) {
            return false;//TODO - временная мера из-за отсутствия STUDENT_REPO_POSTGRES_IMPL
        }
        Student student = repo.getStudentIfExistsOrGetSpecialValue(login, password);

        if (student != null && STUDENT_NOT_EXISTS != student) {
            if (STUDENT_PASSWORD_WRONG != student) {
                createSessionAndSetAttribute(student, req);
                log.info("Логин и пароль студента совпали. Студент входит в систему. Форвард на STUDENT_FRONT_PAGE");
                forward(STUDENT_FRONT_PAGE, "добро пожаловать TEACHER", req, res);
                return true;
            } else {
                forwardError(LOGIN_PAGE, "Введён неверный пароль.", req, res);
                log.info("Логин совпал а пароль не верен. STUDENT- в доступе отказано. Форвард на LOGIN_PAGE");
            }
        }
        return false;
    }
}