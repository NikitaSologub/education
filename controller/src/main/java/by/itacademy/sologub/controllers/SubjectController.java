package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Subject;
import by.itacademy.sologub.SubjectRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.SUBJECTS;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ACTION;
import static by.itacademy.sologub.constants.Constant.ADMIN_SUBJECTS_PAGE;
import static by.itacademy.sologub.constants.Constant.DELETE;
import static by.itacademy.sologub.constants.Constant.PUT;
import static by.itacademy.sologub.constants.Constant.SUBJECT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.SUBJECT_REPO;

@WebServlet(SUBJECT_CONTROLLER)
@Slf4j
public class SubjectController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = "Вы на странице предметов";
        refreshSubjectsListAndForward(msg, req, resp);
    }

    private void refreshSubjectsListAndForward(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectRepo subjectRepo = (SubjectRepo) getServletContext().getAttribute(SUBJECT_REPO);
        List<Subject> list = subjectRepo.getSubjectsList();

        log.debug("Список предметов (добавляем к запросу){}", list);
        req.setAttribute(SUBJECTS, list);
        forward(ADMIN_SUBJECTS_PAGE, msg, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectRepo repo = (SubjectRepo) getServletContext().getAttribute(SUBJECT_REPO);
        Subject subject = extractSubjectFromFormWithoutId(req);
        boolean result = repo.putSubjectIfNotExists(subject);

        String msg;
        if (result) {
            msg = "Subject " + subject + " успешно добавлен";
            log.info("Subject {} успешно добавлена", subject);
        } else {
            msg = "Не удалось добавить Subject " + subject;
            log.info("Не удалось добавить Subject {}", subject);
        }
        refreshSubjectsListAndForward(msg, req, resp);
    }

    Subject extractSubjectFromForm(HttpServletRequest req){
        return extractSubjectFromFormWithoutId(req)
                .withId(Integer.parseInt(req.getParameter(ID)));
    }

    Subject extractSubjectFromFormWithoutId(HttpServletRequest req){
        return new Subject()
                .withTitle(req.getParameter(TITLE));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectRepo repo = (SubjectRepo) getServletContext().getAttribute(SUBJECT_REPO);
        Subject s = extractSubjectFromForm(req);
        boolean result = repo.changeSubjectsParametersIfExists(s);
        String msg;
        if (result) {
            msg = "Зарплата " + s + " успешно изменена";
            log.info("Зарплата {} успешно изменена", s);
        } else {
            msg = "Не удалось изменить зарплату " + s;
            log.info("Не удалось изменить зарплату {}", s);
        }
        refreshSubjectsListAndForward(msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SubjectRepo repo = (SubjectRepo) getServletContext().getAttribute(SUBJECT_REPO);
        Subject s = extractSubjectFromForm(req);
        System.out.println(s);
        boolean result = repo.deleteSubject(s);
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