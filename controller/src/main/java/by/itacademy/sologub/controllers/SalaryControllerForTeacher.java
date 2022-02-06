package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Salary;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.services.AverageSalaryService;
import by.itacademy.sologub.services.SalaryService;
import by.itacademy.sologub.services.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Constant.AVERAGE_SALARY;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.TEACHER_SALARIES_VIEW;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SalaryControllerForTeacher {
    private final TeacherService teacherService;
    private final SalaryService salaryService;
    private final AverageSalaryService averageSalaryService;

    @GetMapping("salaries")
    public ModelAndView getAllSalaries(Principal principal) {
        return getModelAndView(principal);
    }

    @PostMapping("salaries/{id}")
    public ModelAndView getAllSalariesAndAverageSalary(Principal principal, @PathVariable(ID) int id) {
        ModelAndView mav = getModelAndView(principal);
        mav.getModel().put(AVERAGE_SALARY, averageSalaryService.getAverageSalary(id));
        return mav;
    }

    private ModelAndView getModelAndView(Principal principal) {
        Teacher teacher = teacherService.getTeacherIfExistsOrGetSpecialValue(principal.getName());
        Set<Salary> salarySet = salaryService.getAllSalariesByTeacherId(teacher.getId());
        ModelAndView mav = new ModelAndView(TEACHER_SALARIES_VIEW);
        mav.getModel().put(TEACHER, teacher);
        mav.getModel().put(OBJECTS_SET, salarySet);
        return mav;
    }
}