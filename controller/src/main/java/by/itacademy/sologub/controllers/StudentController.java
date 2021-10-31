package by.itacademy.sologub.controllers;

import by.itacademy.sologub.*;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.*;
import static by.itacademy.sologub.constants.Constants.CREDENTIAL_ID;

@WebServlet(STUDENT_CONTROLLER)
@Slf4j
public class StudentController extends BaseController{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        Student student = extractStudentFromForm(req);
        boolean result = repo.putStudentIfNotExists(student);

        String msg;
        if (result) {
            msg = "Студент " + req.getParameter(LOGIN) + " успешно добавлен";
            log.info("Студент {} успешно добавлен", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось добавить Студента " + req.getParameter(LOGIN);
            log.info("Не удалось добавить Студента {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_STUDENTS_PAGE, msg, req, resp);
    }

    Student extractStudentFromForm(HttpServletRequest req) {
        Student teacher = new Student()
                .withCredential(new Credential()
                        .withLogin(req.getParameter(LOGIN))
                        .withPassword(req.getParameter(PASSWORD)))
                .withFirstname(req.getParameter(FIRSTNAME))
                .withLastname(req.getParameter(LASTNAME))
                .withPatronymic(req.getParameter(PATRONYMIC))
                .withDateOfBirth(LocalDate.parse(req.getParameter(DATE_OF_BIRTH)));

        log.debug("Из запроса извлечён обьект студента (без id и credential_id) {}", teacher);
        return teacher;
    }

    Student extractStudentFromFormWithIds(HttpServletRequest req){
        Student student = extractStudentFromForm(req);
        student.setId(Integer.parseInt(req.getParameter(ID)));
        student.getCredential().setId(Integer.parseInt(req.getParameter(CREDENTIAL_ID)));
        log.debug("Из запроса извлечён обьект студента {}", student);
        return student;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        Student newTeacherValues = extractStudentFromFormWithIds(req);
        boolean result = repo.changeStudentParametersIfExists(newTeacherValues);
        String msg;
        if (result) {
            msg = "Студент " + req.getParameter(LOGIN) + " успешно изменён";
            log.info("Студент {} успешно изменён", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось изменить Студента " + req.getParameter(LOGIN);
            log.info("Не удалось изменить Студента {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_STUDENTS_PAGE, msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        boolean result = repo.deleteStudent(req.getParameter(LOGIN));
        String msg;
        if (result) {
            msg = "Студент " + req.getParameter(LOGIN) + " успешно удалён";
            log.info("Студент {} успешно удалён", req.getParameter(LOGIN));
        } else {
            msg = "Не удалось удалить Студента " + req.getParameter(LOGIN);
            log.info("Не удалось удалить Студента {}", req.getParameter(LOGIN));
        }
        forward(ADMIN_STUDENTS_PAGE, msg, req, resp);
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