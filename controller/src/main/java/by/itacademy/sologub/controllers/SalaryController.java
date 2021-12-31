package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.services.FacadeService;
import by.itacademy.sologub.services.SalaryService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static by.itacademy.sologub.constants.Attributes.COINS;
import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Constant.ACTION;
import static by.itacademy.sologub.constants.Constant.ADMIN_SALARIES_PAGE;
import static by.itacademy.sologub.constants.Constant.AVERAGE;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;
import static by.itacademy.sologub.constants.Constant.SALARY_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.TEACHER_ID;

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
        SalaryService service = (SalaryService) getServletContext().getAttribute(FACADE_SERVICE);
        Salary salary = extractSalaryFromFormWithoutId(req);
        int teacherId = Integer.parseInt(req.getParameter(TEACHER_ID));
        boolean result = service.putSalaryToTeacher(salary, teacherId);

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
        FacadeService facade = (FacadeService) getServletContext().getAttribute(FACADE_SERVICE);

        Teacher t = facade.getTeacherIfExistsOrGetSpecialValue(req.getParameter(LOGIN));
        t.setSalaries(facade.getAllSalariesByTeacherId(t.getId()));
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
        Salary s = new Salary()
                .withCoins(Integer.parseInt(req.getParameter(COINS)))
                .withDate(LocalDate.parse(req.getParameter(DATE)));
        log.debug("Из запроса извлечён обьект зп (без id) {}", s);
        return s;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SalaryService service = (SalaryService) getServletContext().getAttribute(FACADE_SERVICE);
        Salary s = extractSalaryFromForm(req);

        boolean result = service.changeSalary(s);
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
        SalaryService service = (SalaryService) getServletContext().getAttribute(FACADE_SERVICE);
        int id = Integer.parseInt(req.getParameter(ID));
        boolean result = service.deleteSalary(id);
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
        FacadeService facade = (FacadeService) getServletContext().getAttribute(FACADE_SERVICE);
        req.setAttribute(AVERAGE, facade.getAverageSalary(Integer.parseInt(req.getParameter(ID))));
        refreshTeacherAndForward("Средняя зарплата учителя по итогам всех месяцев работы ", req, res);
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