package by.itacademy.sologub.controllers;

import by.itacademy.sologub.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Constant.OBJECTS_LIST;

@Slf4j
public abstract class AbstractPersonController<T extends User> extends BaseController {

    protected abstract String getRole();

    protected abstract String getUrl();

    protected abstract List<T> getList();

    protected abstract boolean putInRepo(T user);

    protected abstract boolean changeInRepo(T user);

    protected abstract boolean deleteInRepo(T user);

    protected abstract boolean deleteInRepo(String userLogin);

    protected abstract T extractUserFromForm(HttpServletRequest req);

    protected abstract T extractUserFromFormWithIds(HttpServletRequest req);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String msg = "Вы на странице " + getUrl();
        refreshAndForward(msg, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        T user = extractUserFromForm(req);
        boolean result = putInRepo(user);

        String msg;
        if (result) {
            msg = getRole() + " " + req.getParameter(LOGIN) + " успешно добавлен";
            log.info("{} {} успешно добавлен", getRole(), req.getParameter(LOGIN));
        } else {
            msg = "Не удалось добавить " + getRole() + " " + req.getParameter(LOGIN);
            log.info("Не удалось добавить {} {}", getRole(), req.getParameter(LOGIN));
        }
        refreshAndForward(msg, req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        T user = extractUserFromFormWithIds(req);
        boolean result = changeInRepo(user);
        String msg;
        if (result) {
            msg = getRole() + " " + req.getParameter(LOGIN) + " успешно изменён";
            log.info("{} {} успешно изменён", getRole(), req.getParameter(LOGIN));
        } else {
            msg = "Не удалось изменить " + getRole() + " " + req.getParameter(LOGIN);
            log.info("Не удалось изменить {} {}", getRole(), req.getParameter(LOGIN));
        }
        refreshAndForward(msg, req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean result = deleteInRepo(req.getParameter(LOGIN));
        String msg;
        if (result) {
            msg = getRole() + " " + req.getParameter(LOGIN) + " успешно удалён";
            log.info("{} {} успешно удалён", getRole(), req.getParameter(LOGIN));
        } else {
            msg = "Не удалось удалить " + getRole() + " " + req.getParameter(LOGIN);
            log.info("Не удалось удалить {} {}", getRole(), req.getParameter(LOGIN));
        }
        refreshAndForward(msg, req, resp);
    }

    private void refreshAndForward(String msg, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<T> list = getList();

        log.debug("Список {} (добавляем к запросу){}", getRole(), list);
        req.setAttribute(OBJECTS_LIST, list);
        forward(getUrl(), msg, req, resp);
    }
}