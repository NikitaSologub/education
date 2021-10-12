package by.itacademy.sologub.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(LOGOUT_CONTROLLER)
public class LogoutController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute(SESSION_ENTITY,null);
        session.invalidate(); //TODO - проверить работу сессии
        forward(LOGIN_PAGE, req, resp);
    }
}