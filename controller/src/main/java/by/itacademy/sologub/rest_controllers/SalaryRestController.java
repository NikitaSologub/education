package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.AbstractEntity;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.services.SalaryService;
import by.itacademy.sologub.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import static by.itacademy.sologub.constants.Constant.SALARY_ID;
import static by.itacademy.sologub.constants.Constant.TEACHER_ID;
import static by.itacademy.sologub.constants.ConstantObject.SALARY_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@RestController
@RequestMapping("/rest/teachers/{teacherId}/salaries")
public class SalaryRestController {
    private final SalaryService salaryService;
    private final TeacherService teacherService;

    @Autowired
    public SalaryRestController(SalaryService salaryService, TeacherService teacherService) {
        this.salaryService = salaryService;
        this.teacherService = teacherService;
    }

    @GetMapping
    public Set<Salary> getSalariesByStudentId(@PathVariable(TEACHER_ID) int id) {
        return salaryService.getAllSalariesByTeacherId(id);
    }

    @GetMapping("/{salaryId}")
    public Salary getSalaryById(@PathVariable(SALARY_ID) int salaryId, @PathVariable(TEACHER_ID) int teacherId) {
        Teacher t = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        Salary s = salaryService.getSalary(salaryId);
        if (TEACHER_NOT_EXISTS == t || SALARY_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Teacher with id: " + teacherId + "or Salary with id:" + salaryId + " is not exists");
        }
        return s;
    }

    @PostMapping
    public Salary addSalaryIfNotExists(@RequestBody Salary newSalary, @PathVariable(TEACHER_ID) int teacherId) {
        if (salaryService.putSalaryToTeacher(newSalary, teacherId)) {
            Set<Salary> salaries = salaryService.getAllSalariesByTeacherId(teacherId);
            return Collections.max(salaries, Comparator.comparing(AbstractEntity::getId));
        } else {
            return null;
        }
    }

    @PutMapping("/{salaryId}")
    public Salary changeSalary(@PathVariable(SALARY_ID) int salaryId, @PathVariable(TEACHER_ID) int teacherId,
                               @RequestBody Salary newSalary) {
        Teacher t = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        Salary oldSalary = salaryService.getSalary(salaryId);
        if (SALARY_NOT_EXISTS == oldSalary || TEACHER_NOT_EXISTS == t) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Teacher with id: " + teacherId + "or Salary with id:" + salaryId + " is not exists");
        }
        newSalary.setId(salaryId);
        if (salaryService.changeSalary(newSalary)) {
            return oldSalary;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{salaryId}")
    public Salary deleteSalary(@PathVariable(SALARY_ID) int salaryId, @PathVariable(TEACHER_ID) int teacherId) {
        Salary oldSalary = salaryService.getSalary(salaryId);
        Teacher t = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        if (SALARY_NOT_EXISTS == oldSalary || TEACHER_NOT_EXISTS == t) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Teacher with id: " + teacherId + "or Salary with id:" + salaryId + " is not exists");
        }
        if (salaryService.deleteSalary(salaryId)) {
            return oldSalary;
        } else {
            return null;
        }
    }
}