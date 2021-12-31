package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Subject;
import by.itacademy.sologub.services.SubjectService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_SUBJECTS_PAGE;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;
import static by.itacademy.sologub.constants.Constant.SUBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.SUBJECT_CONTROLLER;

@WebServlet(SUBJECT_CONTROLLER)
@Slf4j
public class SubjectController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = "Вы на странице предметов";
        refreshSubjectsListAndForward(msg, req, resp);
    }

    private void refreshSubjectsListAndForward(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        List<Subject> list = service.getSubjectsList();

        log.debug("set предметов (добавляем к запросу){}", list);
        req.setAttribute(SUBJECTS_SET, list);
        forward(ADMIN_SUBJECTS_PAGE, msg, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        Subject s = extractSubjectFromFormWithoutId(req);
        boolean result = service.putSubjectIfNotExists(s);

        String msg;
        if (result) {
            msg = "Subject " + s + " успешно добавлен";
            log.info("Subject {} успешно добавлена", s);
        } else {
            msg = "Не удалось добавить Subject " + s;
            log.info("Не удалось добавить Subject {}", s);
        }
        refreshSubjectsListAndForward(msg, req, resp);
    }

    Subject extractSubjectFromForm(HttpServletRequest req) {
        return extractSubjectFromFormWithoutId(req)
                .withId(Integer.parseInt(req.getParameter(ID)));
    }

    Subject extractSubjectFromFormWithoutId(HttpServletRequest req) {
        return new Subject()
                .withTitle(req.getParameter(TITLE));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        Subject s = extractSubjectFromForm(req);
        boolean result = service.changeSubjectsParametersIfExists(s);
        String msg;
        if (result) {
            msg = "Subject " + s + " успешно изменён";
            log.info("Subject {} успешно изменён", s);
        } else {
            msg = "Не удалось изменить Subject " + s;
            log.info("Не удалось изменить Subject {}", s);
        }
        refreshSubjectsListAndForward(msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        Subject s = extractSubjectFromForm(req);
        System.out.println(s);
        boolean result = service.deleteSubject(s);
        String msg;
        if (result) {
            msg = "Subject по id " + s.getId() + " успешно удалён";
            log.info("Subject по id={} успешно удалён", s.getId());
        } else {
            msg = "Не удалось удалить Subject с id = " + s.getId();
            log.info("Не удалось удалить Subject с id = {}", s.getId());
        }
        refreshSubjectsListAndForward(msg, req, resp);
    }
}