package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.TeacherService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_PAGE;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;

@WebServlet(TEACHER_CONTROLLER)
@Slf4j
public class TeacherController extends AbstractPersonController<Teacher> {
    @Override
    protected String getRole() {
        return String.valueOf(Role.TEACHER);
    }

    @Override
    protected String getUrl() {
        return ADMIN_TEACHERS_PAGE;
    }

    @Override
    protected Set<Teacher> getSet() {
        TeacherService service = (TeacherService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.getTeachersSet();
    }

    @Override
    protected boolean putInRepo(Teacher user) {
        TeacherService service = (TeacherService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.putTeacherIfNotExists(user);
    }

    @Override
    protected boolean changeInRepo(Teacher user) {
        TeacherService service = (TeacherService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.changeTeachersParametersIfExists(user);
    }

    @Override
    protected boolean deleteInRepo(Teacher user) {
        TeacherService service = (TeacherService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.deleteTeacher(user);
    }

    @Override
    protected boolean deleteInRepo(String userLogin) {
        TeacherService service = (TeacherService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.deleteTeacher(userLogin);
    }

    @Override
    protected Teacher extractUserFromForm(HttpServletRequest req) {
        Teacher t = new Teacher()
                .withCredential(new Credential()
                        .withLogin(req.getParameter(LOGIN))
                        .withPassword(req.getParameter(PASSWORD)))
                .withFirstname(req.getParameter(FIRSTNAME))
                .withLastname(req.getParameter(LASTNAME))
                .withPatronymic(req.getParameter(PATRONYMIC))
                .withDateOfBirth(LocalDate.parse(req.getParameter(DATE_OF_BIRTH)));

        log.debug("Из запроса извлечён обьект учителя (без id и credential_id) {}", t);
        return t;
    }

    @Override
    protected Teacher extractUserFromFormWithIds(HttpServletRequest req) {
        Teacher t = extractUserFromForm(req);
        t.setId(Integer.parseInt(req.getParameter(ID)));
        t.getCredential().setId(Integer.parseInt(req.getParameter(CREDENTIAL_ID)));
        log.debug("Из запроса извлечён обьект учителя {}", t);
        return t;
    }
}