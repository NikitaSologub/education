package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.model.User;
import by.itacademy.sologub.services.AdminService;
import by.itacademy.sologub.services.StudentService;
import by.itacademy.sologub.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Constant.ADMIN_FRONT_VIEW;
import static by.itacademy.sologub.constants.Constant.ERROR_MESSAGE;
import static by.itacademy.sologub.constants.Constant.LOGIN_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.LOGIN_VIEW;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.SESSION_ENTITY;
import static by.itacademy.sologub.constants.Constant.STUDENT_FRONT_VIEW;
import static by.itacademy.sologub.constants.Constant.TEACHER_FRONT_VIEW;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.ADMIN_PASSWORD_WRONG;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_PASSWORD_WRONG;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_PASSWORD_WRONG;

@Controller
@RequestMapping(LOGIN_CONTROLLER)
@Slf4j
public class LoginController {
    private final AdminService adminService;
    private final TeacherService teacherService;
    private final StudentService studentService;

    @Autowired
    public LoginController(AdminService adminService, TeacherService teacherService, StudentService studentService) {
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.studentService = studentService;
    }

    @PostMapping
    public ModelAndView logIn(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                              HttpSession session) {
        if (login != null) {
            login = login.trim();//todo - убрать (переработать)
        }
        if (password != null) {
            password = password.trim();//todo - убрать (переработать)
        }
        log.info("Пользователь {} пытается войти в систему. Пароль {}", login, password);

        Student student = studentService.getStudentIfExistsOrGetSpecialValue(login, password);
        Teacher teacher = teacherService.getTeacherIfExistsOrGetSpecialValue(login, password);
        Admin admin = adminService.getAdminIfExistsOrGetSpecialValue(login, password);

        ModelAndView mav = new ModelAndView();
        if (!checkAdminLogIn(mav, admin, session)) {//todo - (переработать) чтобы возвращало неправильно введёе пароль
            if (!checkTeacherLogIn(mav, teacher, session)) {
                if (!checkStudentLogIn(mav, student, session)) {
                    log.info("логина {} нет в системе в доступе отказано.", login);
                    mav.getModel().put(ERROR_MESSAGE, "пользователя с таким логином не существует");
                    mav.setViewName(LOGIN_VIEW);
                }
            }
        }
        return mav;
    }

    private boolean checkAdminLogIn(ModelAndView mav, Admin admin, HttpSession session) {
        if (admin != null && ADMIN_NOT_EXISTS != admin) {
            if (ADMIN_PASSWORD_WRONG != admin) {
                createSessionAndSetAttribute(admin, session);
                log.info("Логин и пароль админа совпали. Админ входит в систему. Форвард на ADMIN_FRONT_PAGE");
                mav.getModel().put(MESSAGE, "добро пожаловать ADMIN");
                mav.setViewName(ADMIN_FRONT_VIEW);
                return true;
            } else {
                log.info("Логин совпал а пароль не верен. ADMIN- в доступе отказано. Форвард на LOGIN_PAGE");
                mav.getModel().put(ERROR_MESSAGE, "Введён неверный пароль.");
                mav.setViewName(LOGIN_VIEW);
            }
        }
        log.info("админа с таким логином не существует в системе");
        return false;
    }

    private boolean checkTeacherLogIn(ModelAndView mav, Teacher teacher, HttpSession session) {
        if (teacher != null && TEACHER_NOT_EXISTS != teacher) {
            if (TEACHER_PASSWORD_WRONG != teacher) {
                createSessionAndSetAttribute(teacher, session);
                log.info("Логин и пароль учителя совпали. Учитель входит в систему. Форвард на TEACHER_FRONT_PAGE");
                mav.getModel().put(MESSAGE, "добро пожаловать TEACHER");
                mav.setViewName(TEACHER_FRONT_VIEW);
                return true;
            } else {
                log.info("Логин совпал а пароль не верен. TEACHER- в доступе отказано. Форвард на LOGIN_PAGE");
                mav.getModel().put(ERROR_MESSAGE, "Введён неверный пароль.");
                mav.setViewName(LOGIN_VIEW);
            }
        }
        log.info("учителя с таким логином не существует в системе");
        return false;
    }

    private boolean checkStudentLogIn(ModelAndView mav, Student student, HttpSession session) {
        if (student != null && STUDENT_NOT_EXISTS != student) {
            if (STUDENT_PASSWORD_WRONG != student) {
                createSessionAndSetAttribute(student, session);
                log.info("Логин и пароль студента совпали. Студент входит в систему. Форвард на STUDENT_FRONT_PAGE");
                mav.getModel().put(MESSAGE, "добро пожаловать STUDENT");
                mav.setViewName(STUDENT_FRONT_VIEW);
                return true;
            } else {
                log.info("Логин совпал а пароль не верен. STUDENT- в доступе отказано. Форвард на LOGIN_PAGE");
                mav.getModel().put(ERROR_MESSAGE, "Введён неверный пароль.");
                mav.setViewName(LOGIN_VIEW);
            }
        }
        log.info("студента с таким логином не существует в системе");
        return false;
    }

    private void createSessionAndSetAttribute(User user, HttpSession session) {
        session.setAttribute(SESSION_ENTITY, user);
        log.info("пользователь {} положен в сессию.", user);
    }
}