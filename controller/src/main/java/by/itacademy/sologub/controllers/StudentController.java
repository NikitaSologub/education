package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.StudentService;
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
import static by.itacademy.sologub.constants.Constant.ADMIN_STUDENTS_VIEW;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.PERSONS_SET;
import static by.itacademy.sologub.constants.Constant.STUDENT_CONTROLLER;

@Controller
@RequestMapping(STUDENT_CONTROLLER)
@Slf4j
public class StudentController extends AbstractController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ModelAndView doGet() {
        return refreshAndForward("Вы на странице " + ADMIN_STUDENTS_VIEW);
    }

    @PostMapping
    public ModelAndView doPost(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                               @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                               @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth) {
        Student student = new Student()
                .withCredential(new Credential()
                        .withLogin(login)
                        .withPassword(password))
                .withFirstname(firstname)
                .withLastname(lastname)
                .withPatronymic(patronymic)
                .withDateOfBirth(LocalDate.parse(dateOfBirth));

        String msg;
        if (studentService.putStudentIfNotExists(student)) {
            msg = Role.STUDENT + " " + login + " успешно добавлен";
            log.info("{} {} успешно добавлен", Role.STUDENT, login);
        } else {
            msg = "Не удалось добавить " + Role.STUDENT + " " + login;
            log.info("Не удалось добавить {} {}", Role.STUDENT, login);
        }
        return refreshAndForward(msg);
    }

    @PutMapping
    public ModelAndView doPut(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                              @RequestParam(ID) int id, @RequestParam(CREDENTIAL_ID) int credentialId,
                              @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                              @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth,
                              HttpServletRequest req) {
        Student student = new Student()
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
        if (studentService.changeStudentParametersIfExists(student)) {
            msg = Role.STUDENT + " " + login + " успешно изменён";
            log.info("{} {} успешно изменён", Role.STUDENT, login);
        } else {
            msg = "Не удалось изменить " + Role.STUDENT + " " + login;
            log.info("Не удалось изменить {} {}", Role.STUDENT, login);
        }
        resetMethod(req);
        return refreshAndForward(msg);
    }

    @DeleteMapping
    public ModelAndView doDelete(HttpServletRequest req, @RequestParam(LOGIN) String login) {
        String msg;
        if (studentService.deleteStudent(login)) {
            msg = Role.STUDENT + " " + login + " успешно удалён";
            log.info("{} {} успешно удалён", Role.STUDENT, login);
        } else {
            msg = "Не удалось удалить " + Role.STUDENT + " " + login;
            log.info("Не удалось удалить {} {}", Role.STUDENT, login);
        }
        resetMethod(req);
        return refreshAndForward(msg);
    }

    private ModelAndView refreshAndForward(String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_STUDENTS_VIEW);
        Set<Student> set = studentService.getStudentsSet();
        log.debug("Set {} (добавляем к запросу){}", Role.STUDENT, set);
        mav.getModel().put(PERSONS_SET, set);
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}