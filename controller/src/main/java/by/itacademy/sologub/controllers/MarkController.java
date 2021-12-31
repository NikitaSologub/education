package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Mark;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.services.FacadeService;
import by.itacademy.sologub.services.MarkService;
import by.itacademy.sologub.services.SubjectService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.POINT;
import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.Constant.ADMIN_STUDENTS_MARKS_PAGE;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;
import static by.itacademy.sologub.constants.Constant.MARK_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.MARK_ID;
import static by.itacademy.sologub.constants.Constant.STUDENT_ID;
import static by.itacademy.sologub.constants.Constant.SUBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.SUBJECT_ID;

@WebServlet(MARK_CONTROLLER)
@Slf4j
public class MarkController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String msg = "Вы на странице оценок пользователя: " + req.getParameter(LOGIN);
        refreshAndForward(msg, req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        MarkService service = (MarkService) getServletContext().getAttribute(FACADE_SERVICE);
        Mark m = extractMarkFromFormWithoutId(req);
        int studentId = Integer.parseInt(req.getParameter(STUDENT_ID));

        String msg;
        if (service.putMarkToStudent(m, studentId)) {
            msg = "Оценка " + m + " успешно добавлена";
            log.info("Оценка {} успешно добавлена", m);
        } else {
            msg = "Не удалось добавить оценку " + m;
            log.info("Не удалось добавить оценку {}", m);
        }
        refreshAndForward(msg, req, res);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        MarkService service = (MarkService) getServletContext().getAttribute(FACADE_SERVICE);
        Mark m = extractMarkFromForm(req);
        String msg;
        if (service.changeMark(m)) {
            msg = "Оценка " + m + " успешно изменена";
            log.debug("Оценка {} успешно изменена", m);
        } else {
            msg = "Не удалось изменить оценку " + m;
            log.debug("Не удалось изменить оценку {}", m);
        }
        refreshAndForward(msg, req, res);
    }

    private Mark extractMarkFromForm(HttpServletRequest req) {
        Mark m = extractMarkFromFormWithoutId(req);
        m.setId(Integer.parseInt(req.getParameter(MARK_ID)));
        log.debug("Из запроса извлечён обьект mark {}", m);
        return m;
    }

    private Mark extractMarkFromFormWithoutId(HttpServletRequest req) {
        Mark m = new Mark()
                .withSubject(extractSubjectFromForm(req))
                .withPoint(Integer.parseInt(req.getParameter(POINT)))
                .withDate(LocalDate.parse(req.getParameter(DATE)));
        log.debug("Из запроса извлекли оценку (без id) {}", m);
        return m;
    }

    private Subject extractSubjectFromForm(HttpServletRequest req) {
        int id = Integer.parseInt(req.getParameter(SUBJECT_ID));
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.getSubjectIfExistsOrGetSpecialValue(id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        MarkService service = (MarkService) getServletContext().getAttribute(FACADE_SERVICE);
        int markId = Integer.parseInt(req.getParameter(MARK_ID));
        String msg;
        if (service.deleteMark(markId)) {
            msg = "Оценка по id " + markId + " успешно удалена";
            log.debug("Оценка по id={} успешно удалена", markId);
        } else {
            msg = "Не удалось удалить оценку с id = " + markId;
            log.debug("Не удалось удалить оценку с id = {}", markId);
        }
        refreshAndForward(msg, req, res);
    }

    private void refreshAndForward(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setSubjectsToView(req);
        setStudentToView(req);
        forward(ADMIN_STUDENTS_MARKS_PAGE, msg, req, resp);
    }

    private void setSubjectsToView(HttpServletRequest req) {
        SubjectService service = (SubjectService) getServletContext().getAttribute(FACADE_SERVICE);
        Set<Subject> set = new HashSet<>(service.getSubjectsList());
        log.debug("Все предметы что есть (добавляем к запросу){}", set);
        req.setAttribute(SUBJECTS_SET, set);
    }

    private void setStudentToView(HttpServletRequest req) {
        FacadeService facade = (FacadeService) getServletContext().getAttribute(FACADE_SERVICE);
        Student s = facade.getStudentIfExistsOrGetSpecialValue(Integer.parseInt(req.getParameter(STUDENT_ID)));
        s.setMarks(facade.getAllMarksByStudentId(s.getId()));
        log.debug("Оценки которые есть у студента (добавляем к запросу){}", s.getMarks());
        req.setAttribute(STUDENT, s);
    }
}