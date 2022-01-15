package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Mark;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.services.MarkService;
import by.itacademy.sologub.services.StudentService;
import by.itacademy.sologub.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.DATE;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.POINT;
import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.Constant.ADMIN_STUDENTS_MARKS_VIEW;
import static by.itacademy.sologub.constants.Constant.MARK_ID;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.STUDENT_ID;
import static by.itacademy.sologub.constants.Constant.SUBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.SUBJECT_ID;

@Controller
@RequestMapping("students/{studentId}/marks")
@Slf4j
public class MarkController extends JspHiddenMethodController {
    private final MarkService markService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    @Autowired
    public MarkController(MarkService markService, StudentService studentService, SubjectService subjectService) {
        this.markService = markService;
        this.studentService = studentService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public ModelAndView getView(@RequestParam(LOGIN) String login, @PathVariable(STUDENT_ID) int studentId) {
        return refreshModelAndView(login, "Вы на странице оценок пользователя: " + login);
    }

    @PostMapping
    public ModelAndView createMark(@PathVariable(STUDENT_ID) int studentId, @RequestParam(LOGIN) String login,
                                   @RequestParam(SUBJECT_ID) int subjectId, @RequestParam(POINT) int point,
                                   @RequestParam(DATE) String date) {
        Mark m = new Mark()
                .withSubject(subjectService.getSubjectIfExistsOrGetSpecialValue(subjectId))
                .withPoint(point)
                .withDate(LocalDate.parse(date));

        String msg;
        if (markService.putMarkToStudent(m, studentId)) {
            msg = "Оценка " + m + " успешно добавлена";
            log.info("Оценка {} успешно добавлена", m);
        } else {
            msg = "Не удалось добавить оценку " + m;
            log.info("Не удалось добавить оценку {}", m);
        }
        return refreshModelAndView(login, msg);
    }

    @PutMapping("/{markId}")
    public ModelAndView updateMark(@PathVariable(MARK_ID) int markId, @PathVariable(STUDENT_ID) int studentId,
                                   @RequestParam(DATE) String date, @RequestParam(SUBJECT_ID) int subjectId,
                                   @RequestParam(LOGIN) String login, @RequestParam(POINT) int point,
                                   HttpServletRequest req) {
        Mark m = new Mark()
                .withSubject(subjectService.getSubjectIfExistsOrGetSpecialValue(subjectId))
                .withId(markId)
                .withPoint(point)
                .withDate(LocalDate.parse(date));

        String msg;
        if (markService.changeMark(m)) {
            msg = "Оценка " + m + " успешно изменена";
            log.debug("Оценка {} успешно изменена", m);
        } else {
            msg = "Не удалось изменить оценку " + m;
            log.debug("Не удалось изменить оценку {}", m);
        }
        resetMethod(req);
        return refreshModelAndView(login, msg);
    }

    @DeleteMapping("/{markId}")
    public ModelAndView deleteMark(@PathVariable(MARK_ID) int markId, @PathVariable(STUDENT_ID) int studentId,
                                   @RequestParam(LOGIN) String login, HttpServletRequest req) {
        String msg;
        if (markService.deleteMark(markId)) {
            msg = "Оценка по id " + markId + " успешно удалена";
            log.debug("Оценка по id={} успешно удалена", markId);
        } else {
            msg = "Не удалось удалить оценку с id = " + markId;
            log.debug("Не удалось удалить оценку с id = {}", markId);
        }
        resetMethod(req);
        return refreshModelAndView(login, msg);
    }

    private ModelAndView refreshModelAndView(String studentLogin, String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_STUDENTS_MARKS_VIEW);
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentLogin);
        log.debug("студент по логину - " + s + " будет отправлен на страницу");
        s.setMarks(markService.getAllMarksByStudentId(s.getId()));
        log.debug("Оценки которые есть у студента (добавляем к запросу){}", s.getMarks());
        mav.getModel().put(STUDENT, s);

        Set<Subject> set = new HashSet<>(subjectService.getSubjectsList());
        mav.getModel().put(SUBJECTS_SET, set);
        log.debug("Все предметы что есть (добавляем к запросу){}", set);

        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}