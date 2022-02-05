package by.itacademy.sologub.controllers.interceptors;

import by.itacademy.sologub.services.authentication.model.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AddAttributeToSessionInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse res, Object handler, ModelAndView mvn) {
        String simpleUserLogin = (String) req.getAttribute("simpleUserLogin");
        if (simpleUserLogin == null) addUserLoginAttribute(req);
    }

    private void addUserLoginAttribute(HttpServletRequest req) {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userLogin = principal.getUsername();
        log.info("simpleUserLogin={} added to ModelAndView", userLogin);
        req.getSession().setAttribute("simpleUserLogin", userLogin);
    }
}