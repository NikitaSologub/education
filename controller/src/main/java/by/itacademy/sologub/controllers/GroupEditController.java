package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Group;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.services.GroupService;
import by.itacademy.sologub.services.TeacherService;
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
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.DESCRIPTION;
import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Attributes.TEACHER;
import static by.itacademy.sologub.constants.Attributes.TITLE;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_EDIT_VIEW;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.PERSONS_SET;
import static by.itacademy.sologub.constants.Constant.TEACHER_ID;

@Controller
@RequestMapping("groups/{groupId}")
@Slf4j
public class GroupEditController extends JspHiddenMethodController {
    private final TeacherService teacherService;
    private final GroupService groupService;

    @Autowired
    public GroupEditController(TeacherService teacherService, GroupService groupService) {
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping
    public ModelAndView getView(@PathVariable(GROUP_ID) int groupId, @RequestParam(TEACHER_ID) int teacherId) {
        return refreshGroupAndForward(teacherId, groupId, "вы на странице редактирования группы");
    }

    @PostMapping("/edit")
    public ModelAndView doPost(@PathVariable(GROUP_ID) int groupId, @RequestParam(TEACHER_ID) int teacherId,
                               @RequestParam(TITLE) String newTitle, @RequestParam(DESCRIPTION) String newDescription) {
        Group newGr = groupService.getGroupById(groupId);
        newGr.setTitle(newTitle);
        newGr.setDescription(newDescription);
        log.debug("Новое значение group={}", newGr);

        String msg;
        if (groupService.changeGroupsParametersIfExists(newGr)) {
            msg = "Параметры " + newGr.getTitle() + " и " + newGr.getDescription() + " заданы как новые для группы";
            log.debug("Параметры группы {} изменены", newGr);
        } else {
            msg = "Новые параметры " + newGr.getTitle() + " и " + newGr.getDescription() + " не изменены";
            log.debug("Параметры группы {} не изменены", newGr);
        }
        return refreshGroupAndForward(teacherId, groupId, msg);
    }

    @PutMapping("/teachers/{teacherId}")
    public ModelAndView appointTeacher(@PathVariable(GROUP_ID) int groupId, @PathVariable(TEACHER_ID) int teacherId,
                                       HttpServletRequest req) {
        Group group = groupService.getGroupById(groupId);
        Teacher newT = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        group.setTeacher(newT);

        String msg;
        if (groupService.changeGroupsParametersIfExists(group)) {
            msg = "Новый учитель " + newT.getCredential().getLogin() + "назначен руководителем";
            log.debug("Параметры группы изменены. Новый учитель {} назначен руководителем", newT);
        } else {
            msg = "Не получилось назначить " + newT.getCredential().getLogin() + "новым руководителем";
            log.debug("Параметры группы не изменены. Новый учитель {} не назначен руководителем", newT);
        }
        resetMethod(req);
        return refreshGroupAndForward(teacherId, groupId, msg);
    }

    @DeleteMapping("/teachers/{teacherId}")
    public ModelAndView removeTeacher(@PathVariable(GROUP_ID) int groupId, @PathVariable(TEACHER_ID) int teacherId,
                                      HttpServletRequest req) {
        Group group = groupService.getGroupById(groupId);
        Teacher oldT = group.getTeacher();
        String login;
        try {
            login = oldT.getCredential().getLogin();
        } catch (NullPointerException e) {
            login = "ПУСТО";
        }
        group.setTeacher(null);

        String msg;
        if (groupService.changeGroupsParametersIfExists(group)) {
            msg = "Учитель " + login + " снят с должности руководителя группы";
            log.debug("Параметры группы изменены. Учитель {} снят с должности руководителя группы", oldT);
        } else {
            msg = "Не получилось снять " + login + " с поста руководителя группы";
            log.debug("Параметры группы не изменены. Новый учитель {} не назначен руководителем", oldT);
        }
        resetMethod(req);
        return refreshGroupAndForward(teacherId, groupId, msg);
    }

    private ModelAndView refreshGroupAndForward(int teacherId, int id, String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_GROUP_EDIT_VIEW);
        Set<Teacher> teacherSet = teacherService.getTeachersSet();
        Teacher teacher = teacherService.getTeacherIfExistsOrGetSpecialValue(teacherId);
        Group group = groupService.getGroupById(id);
        log.debug("Группа {} текущий учитель {} и set всх учителей {}", group, teacher, teacherSet);

        mav.getModel().put(PERSONS_SET, teacherSet);
        mav.getModel().put(TEACHER, teacher);
        mav.getModel().put(GROUP, group);
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }
}