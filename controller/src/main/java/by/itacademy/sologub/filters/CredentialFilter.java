package by.itacademy.sologub.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.LOGIN;
import static by.itacademy.sologub.constants.Constant.PASSWORD;

//@WebFilter()
public class CredentialFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain ch) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;

        String login = httpReq.getParameter(LOGIN).trim();
        String password = httpReq.getParameter(PASSWORD).trim();


    }
}
