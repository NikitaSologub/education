package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.services.AverageSalaryService;
import by.itacademy.sologub.services.SalaryService;
import by.itacademy.sologub.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static by.itacademy.sologub.constants.Attributes.COINS;
import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Constant.ADMIN_SALARIES_VIEW;
import static by.itacademy.sologub.constants.Constant.AVERAGE_SALARY;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.SALARY_ID;
import static by.itacademy.sologub.constants.Constant.TEACHER_ID;

@Controller
@RequestMapping("teachers/{teacherId}/salaries")
@Slf4j
public class SalaryController extends JspHiddenMethodController {
    private final TeacherService teacherService;
    private final SalaryService salaryService;
    private final AverageSalaryService averageSalaryService;

    @Autowired
    public SalaryController(TeacherService teacherService, SalaryService salaryService, AverageSalaryService avgService) {
        this.teacherService = teacherService;
        this.salaryService = salaryService;
        this.averageSalaryService = avgService;
    }

    @GetMapping
    public ModelAndView getView(@PathVariable(TEACHER_ID) int teacherId) {
        return refreshTeacherAndForward(teacherId, "Вы на странице зарплат пользователя ", new ModelAndView());
    }

    @PostMapping
    public ModelAndView createSalary(@RequestParam(COINS) int coins, @RequestParam(DATE) String date,
                                     @PathVariable(TEACHER_ID) int teacherId) {
        Salary salary = new Salary()
                .withCoins(coins)
                .withDate(LocalDate.parse(date));

        String msg;
        if (salaryService.putSalaryToTeacher(salary, teacherId)) {
            msg = "Зарплата " + salary + " успешно добавлена";
            log.info("Зарплата {} успешно добавлена", salary);
        } else {
            msg = "Не удалось добавить Зарплату " + salary;
            log.info("Не удалось добавить Зарплату {}", salary);
        }
        return refreshTeacherAndForward(teacherId, msg, new ModelAndView());
    }

    @PutMapping("/{salaryId}")
    public ModelAndView updateSalary(@PathVariable(SALARY_ID) int salaryId, @PathVariable(TEACHER_ID) int teacherId,
                                     @RequestParam(COINS) int coins, @RequestParam(DATE) String date,
                                     HttpServletRequest req) {
        Salary s = new Salary()
                .withId(salaryId)
                .withCoins(coins)
                .withDate(LocalDate.parse(date));

        String msg;
        if (salaryService.changeSalary(s)) {
            msg = "Зарплата " + s + " успешно изменена";
            log.info("Зарплата {} успешно изменена", s);
        } else {
            msg = "Не удалось изменить зарплату " + s;
            log.info("Не удалось изменить зарплату {}", s);
        }
        resetMethod(req);
        return refreshTeacherAndForward(teacherId, msg, new ModelAndView());
    }

    @DeleteMapping("/{salaryId}")
    public ModelAndView deleteSalary(@PathVariable(SALARY_ID) int salaryId, @PathVariable(TEACHER_ID) int teacherId,
                                     HttpServletRequest req) {
        String msg;
        if (salaryService.deleteSalary(salaryId)) {
            msg = "Зарплата по id " + salaryId + " успешно удалена";
            log.info("Зарплата по id={} успешно удалена", salaryId);
        } else {
            msg = "Не удалось удалить зарплату с id = " + salaryId;
            log.info("Не удалось удалить зарплату с id = {}", salaryId);
        }
        resetMethod(req);
        return refreshTeacherAndForward(teacherId, msg, new ModelAndView());
    }

    @PatchMapping
    public ModelAndView getViewAndAverageSalary(@PathVariable(TEACHER_ID) int teacherId, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.getModel().put(AVERAGE_SALARY, averageSalaryService.getAverageSalary(teacherId));
        resetMethod(req);
        return refreshTeacherAndForward(teacherId, "Средняя зарплата учителя по итогам всех месяцев работы ", mav);
    }

    private ModelAndView refreshTeacherAndForward(int teacherId, String msg, ModelAndView mav) {
        Teacher t = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        t.setSalaries(salaryService.getAllSalariesByTeacherId(t.getId()));
        log.debug("зарплаты которые имет учитель (добавляем к запросу){}", t.getSalaries());

        mav.getModel().put(TEACHER, t);
        mav.getModel().put(MESSAGE, msg);
        mav.setViewName(ADMIN_SALARIES_VIEW);
        return mav;
    }
}