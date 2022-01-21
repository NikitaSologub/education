package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Subject;
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

import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_SUBJECTS_VIEW;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.SUBJECTS_SET;

@Controller
@RequestMapping("subjects")
@Slf4j
public class SubjectController {
    private final SubjectService service;

    @Autowired
    public SubjectController(SubjectService service) {
        this.service = service;
    }

    @GetMapping
    public ModelAndView getView() {
        return refreshModelAndView("Вы на странице предметов");
    }

    @PostMapping
    public ModelAndView createSubject(@RequestParam(TITLE) String title) {
        Subject s = new Subject()
                .withTitle(title);

        String msg;
        if (service.putSubjectIfNotExists(s)) {
            msg = "Subject " + title + " успешно добавлен";
            log.info("Subject {} успешно добавлена", title);
        } else {
            msg = "Не удалось добавить Subject " + title;
            log.info("Не удалось добавить Subject {}", title);
        }
        return refreshModelAndView(msg);
    }

    @PutMapping("/{id}")
    public ModelAndView updateSubject(@PathVariable(ID) int id, @RequestParam(TITLE) String title) {
        Subject s = new Subject()
                .withId(id)
                .withTitle(title);

        String msg;
        if (service.changeSubjectsParametersIfExists(s)) {
            msg = "Subject " + title + " успешно изменён";
            log.info("Subject {} успешно изменён", title);
        } else {
            msg = "Не удалось изменить Subject " + title;
            log.info("Не удалось изменить Subject {}", title);
        }
        return refreshModelAndView(msg);
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteSubject(@PathVariable(ID) int id, @RequestParam(TITLE) String title) {
        Subject s = new Subject()
                .withId(id)
                .withTitle(title);

        String msg;
        if (service.deleteSubject(s)) {
            msg = "Subject по id " + id + " успешно удалён";
            log.info("Subject по id={} успешно удалён", id);
        } else {
            msg = "Не удалось удалить Subject с id = " + id;
            log.info("Не удалось удалить Subject с id = {}", id);
        }
        return refreshModelAndView(msg);
    }

    private ModelAndView refreshModelAndView(String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_SUBJECTS_VIEW);
        List<Subject> list = service.getSubjectsList();
        log.debug("set предметов (добавляем к запросу){}", list);
        mav.getModel().put(SUBJECTS_SET, list);
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}