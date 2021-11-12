package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.DESCRIPTION;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUPS_PAGE;
import static by.itacademy.sologub.constants.Constant.GROUP_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.GROUP_LIST;
import static by.itacademy.sologub.constants.Constant.GROUP_REPO;

@WebServlet(GROUP_CONTROLLER)
@Slf4j
public class GroupController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        refreshGroupsAndForward("Вы на странице учебных групп", req, resp);
    }

    private void refreshGroupsAndForward(String msg, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        GroupRepo groupRepo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        List<Group> groups = groupRepo.getGroups();

        log.debug("Группы(добавляем к запросу){}", groups);
        req.setAttribute(GROUP_LIST, groups);

        forward(ADMIN_GROUPS_PAGE, msg, req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GroupRepo repo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        Group g = extractGroupOnlyTitleAndDescription(req);

        boolean result = repo.putGroupIfNotExists(g);
        String msg;
        if (result) {
            msg = "Группа " + g + " успешно создана";
            log.info("Группа по id={} успешно создана", g);
        } else {
            msg = "Не удалось создать группу " + g;
            log.info("Не удалось создать группу с id = {}", g);
        }
        refreshGroupsAndForward(msg, req, resp);
    }

    Group extractGroupOnlyTitleAndDescription(HttpServletRequest req) {
        Group g = new Group()
                .withTitle((req.getParameter(TITLE)))
                .withDescription(req.getParameter(DESCRIPTION));

        log.debug("Из запроса извлечён обьект группы (без id, teacher, subjects and students) {}", g);
        return g;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Group g = extractGroupOnlyTitleAndDescription(req);
        //todo - я пока не знаю что делать с редактированием вооюще
        refreshGroupsAndForward("", req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter(ID));

        GroupRepo repo = (GroupRepo) getServletContext().getAttribute(GROUP_REPO);
        boolean result = repo.deleteGroupIfExists(id);
        String msg;
        if (result) {
            msg = "Группа по id " + id + " успешно удалена";
            log.info("Группа по id={} успешно удалена", id);
        } else {
            msg = "Не удалось удалить группу с id = " + id;
            log.info("Не удалось удалить группу с id = {}", id);
        }
        refreshGroupsAndForward(msg, req, resp);
    }
}