package by.itacademy.sologub.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.ADMIN_FRONT_PAGE;
import static by.itacademy.sologub.constants.Constant.ADMIN_SALARIES_PAGE;
import static by.itacademy.sologub.constants.Constant.ADMIN_STUDENTS_PAGE;
import static by.itacademy.sologub.constants.Constant.ADMIN_SUBJECTS_PAGE;
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_PAGE;
import static by.itacademy.sologub.constants.Constant.LOGIN_PAGE;
import static by.itacademy.sologub.constants.Constant.SALARY_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.SESSION_ENTITY;
import static by.itacademy.sologub.constants.Constant.STUDENT_FRONT_PAGE;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.TEACHER_FRONT_PAGE;

@WebFilter(urlPatterns = {ADMIN_FRONT_PAGE, STUDENT_FRONT_PAGE, TEACHER_FRONT_PAGE, ADMIN_TEACHERS_PAGE,
        ADMIN_SALARIES_PAGE, ADMIN_STUDENTS_PAGE, SALARY_CONTROLLER, TEACHER_CONTROLLER, ADMIN_SUBJECTS_PAGE})
@Slf4j
public class AuthorisationFilter extends BaseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession(false);

        if (session == null || session.getAttribute(SESSION_ENTITY) == null) {
            log.info("атрибут {} отсутствует. перенаправляем по url {}", SESSION_ENTITY, LOGIN_PAGE);
            forwardLoginPage("Вы не авторизировались. Пожалуйста войдите в систему", httpReq, res);
        } else {
            log.info("атрибут {} есть продолжаем процесс авторизации", SESSION_ENTITY);
            ch.doFilter(httpReq, res);
        }
    }
}