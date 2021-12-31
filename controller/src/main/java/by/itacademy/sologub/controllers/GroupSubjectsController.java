package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.services.FacadeService;
import by.itacademy.sologub.services.GroupService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_SUBJECTS_PAGE;
import static by.itacademy.sologub.constants.Constant.CURRENT_GROUP_OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.GROUP_SUBJECTS_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.SUBJECT_ID;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;

@WebServlet(GROUP_SUBJECTS_CONTROLLER)
@Slf4j
public class GroupSubjectsController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        refreshViewAndForward("мы на странице группы по управлению предметами", req, res);
    }

    private void refreshViewAndForward(String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Group g = getGroupById(req);

        req.setAttribute(GROUP, g);
        req.setAttribute(CURRENT_GROUP_OBJECTS_SET, g.getSubjects());
        req.setAttribute(OBJECTS_SET, getAllSubjectsToRequest());

        forward(ADMIN_GROUP_SUBJECTS_PAGE, msg, req, res);
    }

    private Set<Subject> getAllSubjectsToRequest() {
        SubjectRepo service = (SubjectRepo) getServletContext().getAttribute(FACADE_SERVICE);
        return new HashSet<>(service.getSubjectsList());
    }

    private Group getGroupById(HttpServletRequest req) {
        FacadeService facade = (FacadeService) getServletContext().getAttribute(FACADE_SERVICE);

        int groupId = Integer.parseInt(req.getParameter(GROUP_ID));
        Group g = facade.getGroupById(groupId);
        log.debug("Вернули группу по groupId={} c параметрами {}", groupId, g);

        Set<Subject> subjects = facade.getSubjectsByGroupId(groupId);
        log.debug("Вернули Set subjects по groupId={} c параметрами {}", groupId, subjects);

        g.setSubjects(subjects);
        return g;
    }

    private Subject getSubjectById(HttpServletRequest req) {
        SubjectRepo service = (SubjectRepo) getServletContext().getAttribute(FACADE_SERVICE);
        int subjectId = Integer.parseInt(req.getParameter(SUBJECT_ID));
        return service.getSubjectIfExistsOrGetSpecialValue(subjectId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupService service = (GroupService) getServletContext().getAttribute(FACADE_SERVICE);
        Group group = getGroupById(req);
        Subject newS = getSubjectById(req);

        String msg;
        if (service.addSubjectInGroup(group, newS)) {
            msg = "Предмет " + newS.getTitle() + " успешно добавлен в группу " + group.getTitle();
            log.debug("Предмет {} добавлен в группу {}", newS.getTitle(), group.getTitle());
        } else {
            msg = "Не удалось добавить предмет " + newS.getTitle() + " в группу " + group.getTitle();
            log.debug("Не удалось добавить предмет {} в группу {}", newS.getTitle(), group.getTitle());
        }
        refreshViewAndForward(msg, req, res);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupService service = (GroupService) getServletContext().getAttribute(FACADE_SERVICE);
        Group group = getGroupById(req);
        Subject oldS = getSubjectById(req);

        String msg;
        if (service.removeSubjectFromGroup(group, oldS)) {
            msg = "Предмет " + oldS.getTitle() + " успешно удалён из группы " + group.getTitle();
            log.debug("Предмет {} удален из группы {}", oldS.getTitle(), group.getTitle());
        } else {
            msg = "Не удалось удалить предмет " + oldS.getTitle() + " из группы " + group.getTitle();
            log.debug("Не удалось удалить предмет {} из группы {}", oldS.getTitle(), group.getTitle());
        }
        refreshViewAndForward(msg, req, res);
    }
}