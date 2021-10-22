package by.itacademy.sologub.controllers;

import by.itacademy.sologub.SalariesRepo;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(SALARIES_CONTROLLER)
@Slf4j
public class SalaryController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalariesRepo salariesRepo = (SalariesRepo) getServletContext().getAttribute(SALARIES_REPO);
        TeacherRepo teacherRepo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        log.debug("атрибут salariesRepo {} и teacherRepo {} получен", salariesRepo, teacherRepo);

        String teacherLogin = req.getParameter(LOGIN);
        log.debug("параметр teacherLogin {}  получен", teacherLogin);

        Teacher teacher = teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherLogin);
        teacher.setSalaries(salariesRepo.getAllSalariesByTeacherId(teacher.getId()));

        log.debug("зарплаты которые имет учитель (добавляем к запросу){}", teacher.getSalaries());
        req.setAttribute(TEACHER, teacher);

        forward(ADMIN_SALARIES_PAGE, req, resp);
    }
}