package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.services.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static by.itacademy.sologub.constants.Attributes.DESCRIPTION;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUPS_VIEW;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.GROUP_LIST;
import static by.itacademy.sologub.constants.Constant.MESSAGE;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("groups")
@Controller
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public ModelAndView getView() {
        return refreshGroupsAndForward("Вы на странице учебных групп");
    }

    @PostMapping
    public ModelAndView createGroup(@RequestParam(TITLE) String title, @RequestParam(DESCRIPTION) String description) {
        Group g = new Group()
                .withTitle(title)
                .withDescription(description);

        String msg;
        if (groupService.putGroupIfNotExists(g)) {
            msg = "Группа " + g + " успешно создана";
            log.info("Группа по id={} успешно создана", g);
        } else {
            msg = "Не удалось создать группу " + g;
            log.info("Не удалось создать группу с id = {}", g);
        }
        return refreshGroupsAndForward(msg);
    }

    @DeleteMapping("/{groupId}")
    public ModelAndView deleteGroup(@PathVariable(GROUP_ID) int id) {
        String msg;
        if (groupService.deleteGroupIfExists(id)) {
            msg = "Группа по id " + id + " успешно удалена";
            log.info("Группа по id={} успешно удалена", id);
        } else {
            msg = "Не удалось удалить группу с id = " + id;
            log.info("Не удалось удалить группу с id = {}", id);
        }
        return refreshGroupsAndForward(msg);
    }

    private ModelAndView refreshGroupsAndForward(String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_GROUPS_VIEW);
        List<Group> groups = groupService.getGroups();
        log.debug("Группы(добавляем к запросу){}", groups);
        mav.getModel().put(GROUP_LIST, groups);
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}