package by.itacademy.sologub.filters;

import by.itacademy.sologub.wrappers.HiddenMethodWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.GROUP_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.HIDDEN_METHOD_PARAMETER;
import static by.itacademy.sologub.constants.Constant.LOGIN_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.LOGOUT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.MARK_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.SALARY_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.STUDENT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.SUBJECT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;

@WebFilter({LOGIN_CONTROLLER, LOGOUT_CONTROLLER, TEACHER_CONTROLLER, STUDENT_CONTROLLER, SALARY_CONTROLLER,
        GROUP_CONTROLLER, SUBJECT_CONTROLLER, MARK_CONTROLLER})
@Slf4j
public class HiddenMethodFieldFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws ServletException, IOException {
        log.debug("Запрос пришел в HiddenMethodFieldFilter от {}", req.getRemoteAddr());

        HttpServletRequest httpReq = (HttpServletRequest) req;
        String hiddenMethod = req.getParameter(HIDDEN_METHOD_PARAMETER);
        if (hiddenMethod != null) {
            HiddenMethodWrapper wrapper = new HiddenMethodWrapper(httpReq, hiddenMethod);
            log.debug("Оборачиваем request в обёртку и ставим метод {}", hiddenMethod);
            ch.doFilter(wrapper, res);
            log.debug("возвращение в фильтр обёртки HiddenMethodWrapper");
        } else {
            log.debug("Параметер скрытого метода=null. Отправляем тот же http request без обёртки");
            ch.doFilter(req, res);
            log.debug("возвращение в фильтр экземпляра httpRequest");
        }
    }
}