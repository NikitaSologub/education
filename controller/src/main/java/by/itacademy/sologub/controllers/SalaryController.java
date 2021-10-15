package by.itacademy.sologub.controllers;

import by.itacademy.sologub.SalariesRepo;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public static final Logger log = LoggerFactory.getLogger(SalaryController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalariesRepo salariesRepo = (SalariesRepo) getServletContext().getAttribute(SALARIES_REPO);
        TeacherRepo teacherRepo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);

        log.debug("атрибут salariesRepo получен в SalaryController {}", salariesRepo);
        log.debug("атрибут teacherRepo получен в SalaryController {}", teacherRepo);

        String teacherLogin = req.getParameter(LOGIN);

        Teacher teacher = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherLogin);
        teacher.setSalaries(salariesRepo.getAllSalariesByTeacherId(teacher.getId()));

        log.debug("зарплаты {} которые имет учитель",teacher.getSalaries());
        req.setAttribute(TEACHER, teacher);

        forward(ADMIN_SALARIES_PAGE, req, resp);
    }
}