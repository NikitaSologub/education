package by.itacademy.sologub.filters;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebFilter(LOGIN_PAGE)
@Slf4j
public class SessionFilter extends BaseFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpSession session = httpReq.getSession(false);

        if (session != null && session.getAttribute(SESSION_ENTITY) != null) {
            log.info("Сессия установлена. Пытаемся идентифицировать роль пользователя");
            User user = (User) session.getAttribute(SESSION_ENTITY);
            Class<? extends User> userClass = user.getClass();

            if (Admin.class == userClass) {
                log.info("Сессия установлена роль {} переходим по url {}", userClass.getName(), ADMIN_FRONT_PAGE);
                forward(ADMIN_FRONT_PAGE, "Добро пожаловать" + userClass.getName(), req, res);
            } else if (Teacher.class == userClass) {
                log.info("Сессия установлена роль {} переходим по url {}", userClass.getName(), TEACHER_FRONT_PAGE);
                forward(TEACHER_FRONT_PAGE, "Добро пожаловать" + userClass.getName(), req, res);
            } else if (Student.class == userClass) {
                log.info("Сессия установлена роль {} переходим по url {}", userClass.getName(), STUDENT_FRONT_PAGE);
                forward(STUDENT_FRONT_PAGE, "Добро пожаловать" + userClass.getName(), req, res);
            } else {
                log.info("Сессия установлена но роль пользователя не подлежит обслуживанию");
                log.info("Инвалидируем сессию и переходим на страницу login_page");
                session.invalidate();
                forward(LOGIN_PAGE, "пользователь с такой ролью не предусмотрен в системе", req, res);
            }
        } else {
            log.info("Сессия не установлена. Переходим на страницу login_page");
            ch.doFilter(req, res);
        }
    }
}