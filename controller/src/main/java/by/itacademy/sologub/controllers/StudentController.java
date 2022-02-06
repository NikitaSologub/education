package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

@Controller
@RequestMapping("students")
@Slf4j
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public ModelAndView getView() {
        return refreshAndForward("Вы на странице " + ADMIN_STUDENTS_VIEW);
    }

    @PostMapping
    public ModelAndView createStudent(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
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

    @PutMapping("/{id}")
    public ModelAndView updateStudent(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                                      @PathVariable(ID) int id, @RequestParam(CREDENTIAL_ID) int credentialId,
                                      @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                                      @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth) {
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
        return refreshAndForward(msg);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteStudent(@PathVariable(ID) int id, @RequestParam(LOGIN) String login) {
        Student student = studentService.getStudentIfExistsOrGetSpecialValue(id);

        String msg;
        if (studentService.deleteStudent(student)) {
            msg = Role.STUDENT + " " + login + " успешно удалён";
            log.info("{} {} успешно удалён", Role.STUDENT, login);
        } else {
            msg = "Не удалось удалить " + Role.STUDENT + " " + login;
            log.info("Не удалось удалить {} {}", Role.STUDENT, login);
        }
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