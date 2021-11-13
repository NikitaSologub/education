package by.itacademy.sologub.controllers;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.LOGIN_PAGE;
import static by.itacademy.sologub.constants.Constant.LOGOUT_CONTROLLER;

@WebServlet(LOGOUT_CONTROLLER)
@Slf4j
public class LogoutController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession()
                .invalidate();
        log.info("Инвалидируем сессию");
        forward(LOGIN_PAGE, req, resp);
    }
}