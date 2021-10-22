package by.itacademy.sologub.filters;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebFilter(urlPatterns = {ADMIN_FRONT_PAGE, STUDENT_FRONT_PAGE, TEACHER_FRONT_PAGE,
        ADMIN_TEACHERS_PAGE, ADMIN_SALARIES_PAGE, SALARIES_CONTROLLER, TEACHER_CONTROLLER})
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