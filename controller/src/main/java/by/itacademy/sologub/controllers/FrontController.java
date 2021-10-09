package by.itacademy.sologub.controllers;

import by.itacademy.sologub.User;
import by.itacademy.sologub.role.Role;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(FRONT_CONTROLLER)
public class FrontController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession(false);
//        if (session != null) {
//            User user = (User) session.getAttribute("SESSION_ENTITY");
//            if (Role.ADMIN == user.getRole()) {
//                forward(LOGIN_PAGE, req, resp);   TODO - проверить работу сессии
//            } else if (Role.TEACHER == user.getRole()) {
//                forward(LOGIN_PAGE, req, resp);
//            } else if (Role.STUDENT == user.getRole()) {
//                forward(LOGIN_PAGE, req, resp);
//            }
//        }
        forward(LOGIN_PAGE, req, resp);
    }
}