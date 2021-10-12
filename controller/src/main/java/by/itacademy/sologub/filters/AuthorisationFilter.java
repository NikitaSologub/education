package by.itacademy.sologub.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebFilter(urlPatterns = {ADMIN_FRONT_PAGE, STUDENT_FRONT_PAGE, TEACHER_FRONT_PAGE})
public class AuthorisationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        HttpSession session = httpReq.getSession(false);

        if (session == null || session.getAttribute(SESSION_ENTITY) == null) {
            RequestDispatcher rd = httpReq.getRequestDispatcher(LOGIN_PAGE);
            httpReq.setAttribute(ERROR_MESSAGE, "Вы не авторизировались. Пожалуйста войдите в систему");
            rd.forward(httpReq, httpRes);
        } else {
            ch.doFilter(req, res);
        }
    }
}