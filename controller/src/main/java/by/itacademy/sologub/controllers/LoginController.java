package by.itacademy.sologub.controllers;

import by.itacademy.sologub.User;
import by.itacademy.sologub.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.ADMIN_CREDENTIAL;
import static by.itacademy.sologub.constants.Constants.ADMIN_USER;

@WebServlet(LOGIN_CONTROLLER)
public class LoginController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String login = req.getParameter(Constant.LOGIN).trim();
        String password = req.getParameter(Constant.PASSWORD).trim();

        checkForAdmin(login, password, req, res);
        checkForTeacher(login, password, req, res);
        checkForStudent(login, password, req, res);
    }

    void checkForAdmin(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String adminLogin = ADMIN_CREDENTIAL.getLogin();
        String adminPassword = ADMIN_CREDENTIAL.getPassword();

        if (adminLogin.equals(login)) {
            if (adminPassword.equals(password)) {
//                setSessionAttribute(ADMIN_USER, req);//TODO - проверить работу сессии
                LOG.info("Логин и пароль администратора совпали. Админ входит в систему. Форвард на ADMIN_FRONT_PAGE");
                forward(ADMIN_FRONT_PAGE, "добро пожаловать ADMIN", req, res);
            } else {
                forwardError(LOGIN_PAGE, "вы ввели неверный пароль", req, res);
                LOG.info("Логин совпал а пароль не верен. АДМИН- в доступе отказано. Форвард на LOGIN_PAGE");
            }
        }
    }

    void setSessionAttribute(User user, HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_ENTITY, user);
    }

    void checkForTeacher(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //временная заглушка - делать то же что и с админом
        //а именно - достать из servletContext обьект TeacherRepo
        // а из него достать по паролю и логину объект Teacher
        //далее - проверить на соответствие и выбрать одно из трёх
        // если пароль и логин правильные - forward на TEACHER_FRONT_PAGE + установить SESSION
        // если пароль не правильный - forwardError(LOGIN_PAGE - вы ввели неверный пароль
        // если логин не правильный - forwardError(LOGIN_PAGE - такого пользователя не существует
        LOG.info("Логин не верен. УЧИТЕЛЬ- в доступе отказано. Форвард на LOGIN_PAGE");
        forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
    }

    void checkForStudent(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //временная заглушка - делать то же что и с админом
        //а именно - достать из servletContext обьект StudentRepo
        // а из него достать по паролю и логину объект Student
        //далее - проверить на соответствие и выбрать одно из трёх
        // если пароль и логин правильные - forward на Student_FRONT_PAGE + установить SESSION
        // если пароль не правильный - forwardError(LOGIN_PAGE - вы ввели неверный пароль
        // если логин не правильный - forwardError(LOGIN_PAGE - такого пользователя не существует
        LOG.info("Логин не верен. СТУДЕНТ- в доступе отказано. Форвард на LOGIN_PAGE");
        forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
    }
}