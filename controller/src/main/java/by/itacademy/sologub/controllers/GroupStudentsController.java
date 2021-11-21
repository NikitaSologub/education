package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.StudentRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_STUDENTS_PAGE;
import static by.itacademy.sologub.constants.Constant.CURRENT_GROUP_OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.GROUP_REPO;
import static by.itacademy.sologub.constants.Constant.GROUP_STUDENTS_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.STUDENT_LOGIN;
import static by.itacademy.sologub.constants.Constant.STUDENT_REPO;

@WebServlet(GROUP_STUDENTS_CONTROLLER)
@Slf4j
public class GroupStudentsController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        refreshViewAndForward("вы на странице управления студентами группы", req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        Group group = getGroupById(req);
        Student newS = getStudentByLogin(req);

        String msg;
        if (groupRepo.addStudentInGroup(group, newS)) {
            msg = "Ученик " + newS.getLastname() + " успешно добавлен в группу " + group.getTitle();
            log.debug("Ученик {} добавлен в группу {}", newS.getLastname(), group.getTitle());
        } else {
            msg = "Не удалось добавить ученика " + newS.getLastname() + " в группу " + group.getTitle();
            log.debug("Не удалось добавить ученика {} в группу {}", newS.getLastname(), group.getTitle());
        }
        refreshViewAndForward(msg, req, res);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        Group group = getGroupById(req);
        Student oldS = getStudentByLogin(req);

        String msg;
        if (groupRepo.removeStudentFromGroup(group, oldS)) {
            msg = "Ученик " + oldS.getLastname() + " успешно удалён из группы " + group.getTitle();
            log.debug("Ученик {} удален из группы {}", oldS.getLastname(), group.getTitle());
        } else {
            msg = "Не удалось удалить ученика " + oldS.getLastname() + " из группы " + group.getTitle();
            log.debug("Не удалось удалить ученика {} из группы {}", oldS.getLastname(), group.getTitle());
        }
        refreshViewAndForward(msg, req, res);
    }

    private void refreshViewAndForward(String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Group g = getGroupById(req);

        req.setAttribute(GROUP, g);
        req.setAttribute(CURRENT_GROUP_OBJECTS_SET, g.getStudents());
        req.setAttribute(OBJECTS_SET, getAllStudentsToRequest());

        forward(ADMIN_GROUP_STUDENTS_PAGE, msg, req, res);
    }

    private Set<Student> getAllStudentsToRequest() {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        return new HashSet<>(repo.getStudentsSet());
    }

    private Group getGroupById(HttpServletRequest req) {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        int groupId = Integer.parseInt(req.getParameter(GROUP_ID));
        return groupRepo.getGroupById(groupId);
    }

    private Student getStudentByLogin(HttpServletRequest req) {
        StudentRepo repo = (StudentRepo) getServletContext().getAttribute(STUDENT_REPO);
        String login = req.getParameter(STUDENT_LOGIN);
        return repo.getStudentIfExistsOrGetSpecialValue(login);
    }
}