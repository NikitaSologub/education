package by.itacademy.sologub.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.ERROR_MESSAGE;
import static by.itacademy.sologub.constants.Constant.MESSAGE;

public abstract class BaseController extends HttpServlet {
    protected void forward(String url, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
        rd.forward(req, res);
    }

    protected void forward(String url, String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute(MESSAGE, msg);
        forward(url, req, res);
    }

    protected void forwardError(String url, String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute(ERROR_MESSAGE, msg);
        forward(url, req, res);
    }
}