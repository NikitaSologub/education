package by.itacademy.sologub.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(LOGOUT_CONTROLLER)
public class LogoutController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(LogoutController.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession()
                .invalidate();
        LOG.info("Инвалидируем сессию");
        forward(LOGIN_PAGE, req, resp);
    }
}