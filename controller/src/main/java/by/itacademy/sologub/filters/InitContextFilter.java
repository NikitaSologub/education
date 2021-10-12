package by.itacademy.sologub.filters;

import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.factory.ModelRepoFactory;
import by.itacademy.sologub.factory.ModelRepoFactoryHardcodeImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import static by.itacademy.sologub.constants.Constant.CREDENTIAL_REPO;

@WebFilter
public class InitContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ModelRepoFactory factory = ModelRepoFactoryHardcodeImpl.getInstance();
        CredentialRepo credentialRepo = factory.getCredentialRepo();

        ServletContext context = filterConfig.getServletContext();
        context.setAttribute(CREDENTIAL_REPO, credentialRepo);

        //Тут я буду инициализировать все что можно
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
    }
}