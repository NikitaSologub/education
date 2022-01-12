package by.itacademy.sologub.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static by.itacademy.sologub.constants.Constant.LOGIN_VIEW;
import static by.itacademy.sologub.constants.Constant.LOGOUT_CONTROLLER;

@Controller
@RequestMapping(LOGOUT_CONTROLLER)
@Slf4j
public class LogoutController {
    @GetMapping
    protected ModelAndView doGet(HttpSession session) {
        session.invalidate();
        log.info("Инвалидируем сессию");
        return new ModelAndView(LOGIN_VIEW);
    }
}