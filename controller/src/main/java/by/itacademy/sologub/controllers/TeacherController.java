package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Constant.ACTION;
import static by.itacademy.sologub.constants.Constant.ADMIN_TEACHERS_PAGE;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.DELETE;
import static by.itacademy.sologub.constants.Constant.PUT;
import static by.itacademy.sologub.constants.Constant.TEACHER_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.TEACHER_REPO;

@WebServlet(TEACHER_CONTROLLER)
@Slf4j
public class TeacherController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Teacher teacher = extractTeacherFromForm(req);
        boolean result = repo.putTeacherIfNotExists(teacher);

        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter(LOGIN) + " успешно добавлен";
            log.info("Учитель {} успешно добавлен", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось добавить Учителя " + req.getParameter(LOGIN);
            log.info("Не удалось добавить учителя {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    Teacher extractTeacherFromForm(HttpServletRequest req) {
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

    Teacher extractTeacherFromFormWithIds(HttpServletRequest req){
        Teacher teacher = extractTeacherFromForm(req);
        teacher.setId(Integer.parseInt(req.getParameter(ID)));
        teacher.getCredential().setId(Integer.parseInt(req.getParameter(CREDENTIAL_ID)));
        log.debug("Из запроса извлечён обьект учителя {}", teacher);
        return teacher;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Teacher newTeacherValues = extractTeacherFromFormWithIds(req);
        boolean result = repo.changeTeachersParametersIfExists(newTeacherValues);
        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter(LOGIN) + " успешно изменён";
            log.info("Учитель {} успешно изменён", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось изменить учителя " + req.getParameter(LOGIN);
            log.info("Не удалось изменить учителя {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        boolean result = repo.deleteTeacher(req.getParameter(LOGIN));
        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter(LOGIN) + " успешно удалён";
            log.info("Учитель {} успешно удалён", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось удалить чителя " + req.getParameter(LOGIN);
            log.info("Не удалось удалить учителя {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (DELETE.equals(req.getParameter(ACTION))) {
            doDelete(req, resp);
            return;
        } else if (PUT.equals(req.getParameter(ACTION))) {
            doPut(req, resp);
            return;
        }
        super.service(req, resp);
    }
}