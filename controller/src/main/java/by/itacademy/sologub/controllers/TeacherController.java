package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.role.Role;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_PAGE;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.TEACHER_REPO;

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
    protected List<Teacher> getList() {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        return repo.getTeachersList();
    }

    @Override
    protected boolean putInRepo(Teacher user) {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        return repo.putTeacherIfNotExists(user);
    }

    @Override
    protected boolean changeInRepo(Teacher user) {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        return repo.changeTeachersParametersIfExists(user);
    }

    @Override
    protected boolean deleteInRepo(Teacher user) {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        return repo.deleteTeacher(user);
    }

    @Override
    protected boolean deleteInRepo(String userLogin) {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        return repo.deleteTeacher(userLogin);
    }

    @Override
    protected Teacher extractUserFromForm(HttpServletRequest req) {
        Teacher teacher = new Teacher()
                .withCredential(new Credential()
                        .withLogin(req.getParameter(LOGIN))
                        .withPassword(req.getParameter(PASSWORD)))
                .withFirstname(req.getParameter(FIRSTNAME))
                .withLastname(req.getParameter(LASTNAME))
                .withPatronymic(req.getParameter(PATRONYMIC))
                .withDateOfBirth(LocalDate.parse(req.getParameter(DATE_OF_BIRTH)));

        log.debug("Из запроса извлечён обьект учителя (без id и credential_id) {}", teacher);
        return teacher;
    }

    @Override
    protected Teacher extractUserFromFormWithIds(HttpServletRequest req) {
        Teacher teacher = extractUserFromForm(req);
        teacher.setId(Integer.parseInt(req.getParameter(ID)));
        teacher.getCredential().setId(Integer.parseInt(req.getParameter(CREDENTIAL_ID)));
        log.debug("Из запроса извлечён обьект учителя {}", teacher);
        return teacher;
    }
}