package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.TeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_VIEW;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.PERSONS_SET;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;

@Controller
@RequestMapping(TEACHER_CONTROLLER)
@Slf4j
public class TeacherController extends AbstractController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ModelAndView doGet() {
        return refreshAndForward("Вы на странице " + ADMIN_TEACHERS_VIEW);
    }

    @PostMapping
    public ModelAndView doPost(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                               @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                               @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth) {
        Teacher teacher = new Teacher()
                .withCredential(new Credential()
                        .withLogin(login)
                        .withPassword(password))
                .withFirstname(firstname)
                .withLastname(lastname)
                .withPatronymic(patronymic)
                .withDateOfBirth(LocalDate.parse(dateOfBirth));

        String msg;
        if (teacherService.putTeacherIfNotExists(teacher)) {
            msg = Role.TEACHER + " " + login + " успешно добавлен";
            log.info("{} {} успешно добавлен", Role.TEACHER, login);
        } else {
            msg = "Не удалось добавить " + Role.TEACHER + " " + login;
            log.info("Не удалось добавить {} {}", Role.TEACHER, login);
        }
        return refreshAndForward(msg);
    }

    @PutMapping
    public ModelAndView doPut(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                              @RequestParam(ID) int id, @RequestParam(CREDENTIAL_ID) int credentialId,
                              @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                              @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth,
                              HttpServletRequest req) {
        Teacher teacher = new Teacher()
                .withId(id)
                .withCredential(new Credential()
                        .withId(credentialId)
                        .withLogin(login)
                        .withPassword(password))
                .withFirstname(firstname)
                .withLastname(lastname)
                .withPatronymic(patronymic)
                .withDateOfBirth(LocalDate.parse(dateOfBirth));

        String msg;
        if (teacherService.changeTeachersParametersIfExists(teacher)) {
            msg = Role.TEACHER + " " + login + " успешно изменён";
            log.info("{} {} успешно изменён", Role.TEACHER, login);
        } else {
            msg = "Не удалось изменить " + Role.TEACHER + " " + login;
            log.info("Не удалось изменить {} {}", Role.TEACHER, login);
        }
        resetMethod(req);
        return refreshAndForward(msg);
    }

    @DeleteMapping
    public ModelAndView doDelete(@RequestParam(LOGIN) String login, HttpServletRequest req) {
        String msg;
        if (teacherService.deleteTeacher(login)) {
            msg = Role.TEACHER + " " + login + " успешно удалён";
            log.info("{} {} успешно удалён", Role.TEACHER, login);
        } else {
            msg = "Не удалось удалить " + Role.TEACHER + " " + login;
            log.info("Не удалось удалить {} {}", Role.TEACHER, login);
        }
        resetMethod(req);
        return refreshAndForward(msg);
    }

    private ModelAndView refreshAndForward(String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_TEACHERS_VIEW);
        Set<Teacher> set = teacherService.getTeachersSet();
        log.debug("Set {} (добавляем к запросу){}", Role.TEACHER, set);
        mav.getModel().put(PERSONS_SET, set);
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}