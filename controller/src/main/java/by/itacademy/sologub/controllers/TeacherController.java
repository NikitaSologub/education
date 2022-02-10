package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Credential;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.TeacherService;
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
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_VIEW;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.PERSONS_SET;

@Controller
@RequestMapping("teachers")
@Slf4j
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public ModelAndView getView() {
        return refreshAndForward("Вы на странице " + ADMIN_TEACHERS_VIEW);
    }

    @PostMapping
    public ModelAndView createTeacher(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
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

    @PutMapping("/{id}")
    public ModelAndView updateTeacher(@RequestParam(LOGIN) String login, @RequestParam(PASSWORD) String password,
                                      @PathVariable(ID) int id, @RequestParam(CREDENTIAL_ID) int credentialId,
                                      @RequestParam(FIRSTNAME) String firstname, @RequestParam(LASTNAME) String lastname,
                                      @RequestParam(PATRONYMIC) String patronymic, @RequestParam(DATE_OF_BIRTH) String dateOfBirth) {
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
        return refreshAndForward(msg);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteTeacher(@PathVariable(ID) int id, @RequestParam(LOGIN) String login) {
        Teacher teacher = teacherService.getTeacherIfExistsOrGetSpecialValue(id);

        String msg;
        if (teacherService.deleteTeacher(teacher)) {
            msg = Role.TEACHER + " " + login + " успешно удалён";
            log.info("{} {} успешно удалён", Role.TEACHER, login);
        } else {
            msg = "Не удалось удалить " + Role.TEACHER + " " + login;
            log.info("Не удалось удалить {} {}", Role.TEACHER, login);
        }
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