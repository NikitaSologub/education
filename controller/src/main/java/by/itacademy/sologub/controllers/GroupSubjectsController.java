package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.services.GroupService;
import by.itacademy.sologub.services.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.GROUP;
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_SUBJECTS_VIEW;
import static by.itacademy.sologub.constants.Constant.CURRENT_GROUP_OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.SUBJECT_ID;

@Controller
@RequestMapping("groups/{groupId}/subjects")
@Slf4j
public class GroupSubjectsController {
    private final GroupService groupService;
    private final SubjectService subjectService;

    @Autowired
    public GroupSubjectsController(GroupService groupService, SubjectService subjectService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public ModelAndView getView(@PathVariable(GROUP_ID) int groupId) {
        return refreshModelAndView(groupId, "Мы на странице группы по управлению предметами");
    }

    @PostMapping("/{subjectId}")
    public ModelAndView includeSubjectToGroupByIds(@PathVariable(GROUP_ID) int groupId, @PathVariable(SUBJECT_ID) int subjectId) {
        Group group = getGroupByIdWithSubjects(groupId);
        Subject newS = subjectService.getSubjectIfExistsOrGetSpecialValue(subjectId);

        String msg;
        if (groupService.addSubjectInGroup(group, newS)) {
            msg = "Предмет " + newS.getTitle() + " успешно добавлен в группу " + group.getTitle();
            log.debug("Предмет {} добавлен в группу {}", newS.getTitle(), group.getTitle());
        } else {
            msg = "Не удалось добавить предмет " + newS.getTitle() + " в группу " + group.getTitle();
            log.debug("Не удалось добавить предмет {} в группу {}", newS.getTitle(), group.getTitle());
        }
        return refreshModelAndView(groupId, msg);
    }

    @DeleteMapping("/{subjectId}")
    public ModelAndView excludeSubjectFromGroupByIds(@PathVariable(GROUP_ID) int groupId, @PathVariable(SUBJECT_ID) int subjectId) {
        Group group = getGroupByIdWithSubjects(groupId);
        Subject oldS = subjectService.getSubjectIfExistsOrGetSpecialValue(subjectId);

        String msg;
        if (groupService.removeSubjectFromGroup(group, oldS)) {
            msg = "Предмет " + oldS.getTitle() + " успешно удалён из группы " + group.getTitle();
            log.debug("Предмет {} удален из группы {}", oldS.getTitle(), group.getTitle());
        } else {
            msg = "Не удалось удалить предмет " + oldS.getTitle() + " из группы " + group.getTitle();
            log.debug("Не удалось удалить предмет {} из группы {}", oldS.getTitle(), group.getTitle());
        }
        return refreshModelAndView(groupId, msg);
    }

    private ModelAndView refreshModelAndView(int groupId, String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_GROUP_SUBJECTS_VIEW);
        Group g = getGroupByIdWithSubjects(groupId);

        mav.getModel().put(GROUP, g);
        mav.getModel().put(CURRENT_GROUP_OBJECTS_SET, g.getSubjects());
        mav.getModel().put(OBJECTS_SET, new HashSet<>(subjectService.getSubjectsList()));
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }

    private Group getGroupByIdWithSubjects(int groupId) {
        Group g = groupService.getGroupById(groupId);
        log.debug("Вернули группу по groupId={} c параметрами {}", groupId, g);

        Set<Subject> subjects = subjectService.getSubjectsByGroupId(groupId);
        g.setSubjects(subjects);
        log.debug("Вернули Set subjects по groupId={} c параметрами {}", groupId, subjects);
        return g;
    }
}