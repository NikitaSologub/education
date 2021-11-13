package by.itacademy.sologub.filters;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

public abstract class BaseFilter {
    void forwardLoginPage(String msg, ServletRequest req, ServletResponse res) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(LOGIN_PAGE);
        req.setAttribute(ERROR_MESSAGE, msg);
        rd.forward(req, res);
    }

    void forward(String url, String msg, ServletRequest req, ServletResponse res) throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(url);
        req.setAttribute(MESSAGE, msg);
        rd.forward(req, res);
    }
}
