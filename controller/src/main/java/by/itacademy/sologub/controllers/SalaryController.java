package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Salary;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.COINS;
import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Constant.ACTION;
import static by.itacademy.sologub.constants.Constant.ADMIN_SALARIES_PAGE;
import static by.itacademy.sologub.constants.Constant.AVERAGE;
import static by.itacademy.sologub.constants.Constant.SALARY_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.SALARY_REPO;
import static by.itacademy.sologub.constants.Constant.TEACHER_ID;
import static by.itacademy.sologub.constants.Constant.TEACHER_REPO;

@WebServlet(SALARY_CONTROLLER)
@Slf4j
public class SalaryController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = "Вы на странице зарплат пользователя " + req.getParameter(LOGIN);
        refreshTeacherAndForward(msg, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalaryRepo repo = (SalaryRepo) getServletContext().getAttribute(SALARY_REPO);
        Salary salary = extractSalaryFromFormWithoutId(req);
        boolean result = repo.putSalary(salary);

        String msg;
        if (result) {
            msg = "Зарплата " + salary + " успешно добавлена";
            log.info("Зарплата {} успешно добавлена", salary);
        } else {
            msg = "Не удалось добавить Зарплату " + salary;
            log.info("Не удалось добавить Зарплату {}", salary);
        }
        refreshTeacherAndForward(msg, req, resp);
    }

    private void refreshTeacherAndForward(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalaryRepo salaryRepo = (SalaryRepo) getServletContext().getAttribute(SALARY_REPO);
        TeacherRepo teacherRepo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);

        Teacher t = teacherRepo.getTeacherIfExistsOrGetSpecialValue(req.getParameter(LOGIN));
        t.setSalaries(salaryRepo.getAllSalariesByTeacherId(t.getId()));
        log.debug("зарплаты которые имет учитель (добавляем к запросу){}", t.getSalaries());
        req.setAttribute(TEACHER, t);

        forward(ADMIN_SALARIES_PAGE, msg, req, resp);
    }

    Salary extractSalaryFromForm(HttpServletRequest req) {
        Salary s = extractSalaryFromFormWithoutId(req);
        s.setId(Integer.parseInt(req.getParameter(ID)));
        log.debug("Из запроса извлечён обьект зп {}", s);
        return s;
    }

    Salary extractSalaryFromFormWithoutId(HttpServletRequest req) {
        Salary salary = new Salary()
                .withCoins(Integer.parseInt(req.getParameter(COINS)))
                .withDate(LocalDate.parse(req.getParameter(DATE)))
                .withTeacherId(Integer.parseInt(req.getParameter(TEACHER_ID)));
        log.debug("Из запроса извлечён обьект зп (без id) {}", salary);
        return salary;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalaryRepo repo = (SalaryRepo) getServletContext().getAttribute(SALARY_REPO);
        Salary s = extractSalaryFromForm(req);

        boolean result = repo.changeSalary(s);
        String msg;
        if (result) {
            msg = "Зарплата " + s + " успешно изменена";
            log.info("Зарплата {} успешно изменена", s);
        } else {
            msg = "Не удалось изменить зарплату " + s;
            log.info("Не удалось изменить зарплату {}", s);
        }
        refreshTeacherAndForward(msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalaryRepo repo = (SalaryRepo) getServletContext().getAttribute(SALARY_REPO);
        int id = Integer.parseInt(req.getParameter(ID));
        boolean result = repo.deleteSalary(id);
        String msg;
        if (result) {
            msg = "Зарплата по id " + id + " успешно удалена";
            log.info("Зарплата по id={} успешно удалена", id);
        } else {
            msg = "Не удалось удалить зарплату с id = " + id;
            log.info("Не удалось удалить зарплату с id = {}", id);
        }
        refreshTeacherAndForward(msg, req, resp);
    }

    private void doSalariesParameters(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        SalaryRepo repo = (SalaryRepo) getServletContext().getAttribute(SALARY_REPO);
        Set<Salary> salaryList = repo.getAllSalariesByTeacherId(Integer.parseInt(req.getParameter(ID)));

        double average = salaryList.stream()
                .mapToInt(Salary::getCoins)
                .average().orElse(0.0);
        req.setAttribute(AVERAGE, round(average/100));
        refreshTeacherAndForward("Средняя зарплата учителя по итогам всех месяцев работы ",req,res);
    }

    private String round(double val) {
        return String.format("%.2f", val);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (AVERAGE.equals(req.getParameter(ACTION))) {
            doSalariesParameters(req, resp);
            return;
        }
        super.service(req, resp);
    }
}