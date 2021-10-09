package by.itacademy.sologub.controllers;

import by.itacademy.sologub.constants.Constant;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.ADMIN_CREDENTIAL;

@WebServlet(LOGIN_CONTROLLER)
public class LoginController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String login = req.getParameter(Constant.LOGIN).trim();
        String password = req.getParameter(Constant.PASSWORD).trim();

        checkForAdmin(login, password, req, res);
//        checkForTeacher(login, password, req, res);
//        checkForStudent(login, password, req, res);
//        IUserDAO userDAO = DAOFactory.getDAO(IUserDAO.class);
//        boolean checkItOut = userDAO.userVerification(login, password);
//
//        if (checkItOut) {
//            user.setLogin(login);
//            HttpSession session = req.getSession();
//            session.setAttribute(Constant.LOGIN, user.getLogin());
//            req.setAttribute(Constant.KEY_LIST_NAME, ConstantsJSP.MESSAGE_FIRST_MESSAGE_AFTER_LOGIN);
//            forward(Constant.JUMP_TODO, req, res);
//        } else {
//            forwardError(ConstantsJSP.ERROR_WRONG_LOGIN_OR_PASSWORD, req, res);
//        }
    }

    void checkForAdmin(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String adminLogin = ADMIN_CREDENTIAL.getLogin();
        String adminPassword = ADMIN_CREDENTIAL.getPassword();

        if (adminLogin.equals(login)) {
            if (adminPassword.equals(password)) {
                //положить в сессию - придумать атрибут
                forward(ADMIN_FRONT_PAGE, "добро пожаловать ADMIN", req, res);
            } else {
                forwardError(LOGIN_PAGE, "вы ввели неверный пароль", req, res);
            }
        } else {
            //удалить потом
            forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
        }
    }

    void checkForTeacher(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //временная заглушка - делать то же что и с админом
        //достать репозиторий и проверять так же как и админа
        forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
    }

    void checkForStudent(String login, String password, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //временная заглушка - делать то же что и с админом
        //достать репозиторий и проверять так же как и админа
        forwardError(LOGIN_PAGE, "пользователя с таким логином не существует", req, res);
    }
}