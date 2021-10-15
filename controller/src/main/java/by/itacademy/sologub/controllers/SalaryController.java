package by.itacademy.sologub.controllers;

import by.itacademy.sologub.SalariesRepo;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Teacher;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(SALARIES_CONTROLLER)
public class SalaryController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        SalariesRepo salariesRepo = (SalariesRepo) context.getAttribute(SALARIES_REPO);
        Teacher teacher = (Teacher) req.getAttribute(TEACHER);
        List<Salary> salaries = salariesRepo.getAllSalariesByTeacherId(teacher.getId());
        teacher.setSalaries(salaries);

        req.setAttribute(TEACHER, teacher);
        forward("", req, resp);
        super.doPost(req, resp);
    }
}