package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.DESCRIPTION;
import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_EDIT_PAGE;
import static by.itacademy.sologub.constants.Constant.GROUP_EDIT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.GROUP_REPO;
import static by.itacademy.sologub.constants.Constant.PERSONS_SET;
import static by.itacademy.sologub.constants.Constant.TEACHER_LOGIN;
import static by.itacademy.sologub.constants.Constant.TEACHER_REPO;

@WebServlet(GROUP_EDIT_CONTROLLER)
@Slf4j
public class GroupEditController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        refreshGroupAndForward("переход на страницу редактирования параметров группы", req, resp);
    }

    private void refreshGroupAndForward(String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        TeacherRepo teacherRepo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        Set<Teacher> teacherSet = teacherRepo.getTeachersSet();
        Teacher t = getTeacherById(req);
        Group group = getGroupById(req);

        req.setAttribute(TEACHER, t);
        req.setAttribute(PERSONS_SET, teacherSet);
        req.setAttribute(GROUP, group);

        log.debug("Группа {} текущий учитель {} и set всх учителей {}", group, t, teacherSet);
        forward(ADMIN_GROUP_EDIT_PAGE, msg, req, res);
    }

    private Teacher getTeacherById(HttpServletRequest req) {
        TeacherRepo teacherRepo = (TeacherRepo) getServletContext().getAttribute(TEACHER_REPO);
        String teacherLogin = req.getParameter(TEACHER_LOGIN);
        return teacherRepo.getTeacherIfExistsOrGetSpecialValue(teacherLogin);
    }

    private Group getGroupById(HttpServletRequest req) {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        int groupId = Integer.parseInt(req.getParameter(GROUP_ID));
        return groupRepo.getGroupById(groupId);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        String newTitle = req.getParameter(TITLE);
        log.debug("Новое значение title={}", newTitle);
        String newDescription = req.getParameter(DESCRIPTION);
        log.debug("Новое значение description={}", newDescription);

        Group newGr = getGroupById(req);
        newGr.setTitle(newTitle);
        newGr.setDescription(newDescription);
        log.debug("Новое значение group={}", newGr);

        String msg;
        if (groupRepo.changeGroupsParametersIfExists(newGr)) {
            msg = "Параметры " + newGr.getTitle() + " и " + newGr.getDescription() + " заданы как новые для группы";
            log.debug("Параметры группы {} изменены", newGr);
        } else {
            msg = "Новые параметры " + newGr.getTitle() + " и " + newGr.getDescription() + " не изменены";
            log.debug("Параметры группы {} не изменены", newGr);
        }
        refreshGroupAndForward(msg, req, res);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        Group group = getGroupById(req);
        Teacher newT = getTeacherById(req);
        group.setTeacher(newT);

        String msg;
        if (groupRepo.changeGroupsParametersIfExists(group)) {
            msg = "Новый учитель " + newT.getCredential().getLogin() + "назначен руководителем";
            log.debug("Параметры группы изменены. Новый учитель {} назначен руководителем", newT);
        } else {
            msg = "Не получилось назначить " + newT.getCredential().getLogin() + "новым руководителем";
            log.debug("Параметры группы не изменены. Новый учитель {} не назначен руководителем", newT);
        }
        refreshGroupAndForward(msg, req, res);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        Group group = getGroupById(req);
        Teacher oldT = group.getTeacher();
        String login;
        try {
            login = oldT.getCredential().getLogin();
        } catch (NullPointerException e) {
            login = "ПУСТО";
        }
        group.setTeacher(null);

        String msg;
        if (groupRepo.changeGroupsParametersIfExists(group)) {
            msg = "Учитель " + login + " снят с должности руководителя группы";
            log.debug("Параметры группы изменены. Учитель {} снят с должности руководителя группы", oldT);
        } else {
            msg = "Не получилось снять " + login + " с поста руководителя группы";
            log.debug("Параметры группы не изменены. Новый учитель {} не назначен руководителем", oldT);
        }
        refreshGroupAndForward(msg, req, res);
    }
}