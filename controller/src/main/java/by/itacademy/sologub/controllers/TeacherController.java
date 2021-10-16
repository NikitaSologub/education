package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.role.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static by.itacademy.sologub.constants.Constant.*;

@WebServlet(TEACHER_CONTROLLER)
public class TeacherController extends BaseController {
    private static final Logger Log = LoggerFactory.getLogger(TeacherController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Teacher teacher = extractTeacherFromForm(req);
        boolean result = repo.putTeacherIfNotExists(teacher);

        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter("login") + " успешно добавлен";
            Log.info("Учитель {} успешно добавлен", req.getParameter("login"));
        } else {
            msg = "Не удалось добавить чителя " + req.getParameter("login");
            Log.info("Не удалось добавить учителя {}", req.getParameter("login"));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    Teacher extractTeacherFromForm(HttpServletRequest req) {
        String firstname = req.getParameter(FIRSTNAME);
        String lastname = req.getParameter(LASTNAME);
        String patronymic = req.getParameter(PATRONYMIC);
        LocalDate dateOfBirth = LocalDate.parse(req.getParameter(DATE_OF_BIRTH));

        Teacher teacher = new Teacher();
        teacher.setCredential(extractCredentialFromForm(req));
        teacher.setFirstname(firstname);
        teacher.setLastname(lastname);
        teacher.setPatronymic(patronymic);
        teacher.setDateOfBirth(dateOfBirth);
        teacher.setRole(Role.TEACHER);

        Log.debug("Из запроса извлечён обьект учителя {}", teacher);
        return teacher;
    }

    Credential extractCredentialFromForm(HttpServletRequest req) {
        String login = req.getParameter(LOGIN);
        String password = req.getParameter(PASSWORD);

        Credential cr = new Credential();
        cr.setLogin(login);
        cr.setPassword(password);

        Log.debug("Из запроса извлечён обьект учётных данных {}", cr);
        return cr;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Teacher newTeacherValues = extractTeacherFromForm(req);
        boolean result = repo.changeTeachersParametersIfExists(newTeacherValues);
        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter("login") + " успешно изменён";
            Log.info("Учитель {} успешно изменён", req.getParameter("login"));
        } else {
            msg = "Не удалось изменить учителя " + req.getParameter("login");
            Log.info("Не удалось изменить учителя {}", req.getParameter("login"));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TeacherRepo repo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        boolean result = repo.deleteTeacher(req.getParameter("teacherLogin"));
        String msg;
        if (result) {
            msg = "Учитель " + req.getParameter("teacherLogin") + " успешно удалён";
            Log.info("Учитель {} успешно удалён", req.getParameter("teacherLogin"));
        } else {
            msg = "Не удалось удалить чителя " + req.getParameter("teacherLogin");
            Log.info("Не удалось удалить учителя {}", req.getParameter("teacherLogin"));
        }
        forward(ADMIN_TEACHERS_PAGE, msg, req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("delete".equals(req.getParameter("action"))) {
            doDelete(req, resp);
            return;
        } else if ("put".equals(req.getParameter("action"))) {
            doPut(req, resp);
            return;
        }
        super.service(req, resp);
    }
}
