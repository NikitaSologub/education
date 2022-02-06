package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.services.GroupService;
import by.itacademy.sologub.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import static by.itacademy.sologub.constants.Constant.ADMIN_GROUP_STUDENTS_VIEW;
import static by.itacademy.sologub.constants.Constant.CURRENT_GROUP_OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.MESSAGE;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.STUDENT_ID;

@Controller
@RequestMapping("/groups/{groupId}/students")
@Slf4j
@RequiredArgsConstructor
public class GroupStudentsController {
    private final GroupService groupService;
    private final StudentService studentService;

    @GetMapping
    public ModelAndView getView(@PathVariable(GROUP_ID) int groupId) {
        return refreshViewAndForward(groupId, "вы на странице управления студентами группы");
    }

    @PostMapping("/{studentId}")
    public ModelAndView includeStudentToGroupByIds(@PathVariable(GROUP_ID) int groupId, @PathVariable(STUDENT_ID) int studentId) {
        Group group = getGroupByIdWithStudents(groupId);
        Student newS = studentService.getStudentIfExistsOrGetSpecialValue(studentId);

        String msg;
        if (groupService.addStudentInGroup(group, newS)) {
            msg = "Ученик " + newS.getLastname() + " успешно добавлен в группу " + group.getTitle();
            log.debug("Ученик {} добавлен в группу {}", newS.getLastname(), group.getTitle());
        } else {
            msg = "Не удалось добавить ученика " + newS.getLastname() + " в группу " + group.getTitle();
            log.debug("Не удалось добавить ученика {} в группу {}", newS.getLastname(), group.getTitle());
        }
        return refreshViewAndForward(groupId, msg);
    }

    @DeleteMapping("/{studentId}")
    public ModelAndView excludeStudentFromGroupByIds(@PathVariable(GROUP_ID) int groupId, @PathVariable(STUDENT_ID) int studentId) {
        Group group = getGroupByIdWithStudents(groupId);
        Student oldS = studentService.getStudentIfExistsOrGetSpecialValue(studentId);

        String msg;
        if (groupService.removeStudentFromGroup(group, oldS)) {
            msg = "Ученик " + oldS.getLastname() + " успешно удалён из группы " + group.getTitle();
            log.debug("Ученик {} удален из группы {}", oldS.getLastname(), group.getTitle());
        } else {
            msg = "Не удалось удалить ученика " + oldS.getLastname() + " из группы " + group.getTitle();
            log.debug("Не удалось удалить ученика {} из группы {}", oldS.getLastname(), group.getTitle());
        }
        return refreshViewAndForward(groupId, msg);
    }

    private ModelAndView refreshViewAndForward(int groupId, String msg) {
        ModelAndView mav = new ModelAndView(ADMIN_GROUP_STUDENTS_VIEW);
        Group g = getGroupByIdWithStudents(groupId);

        mav.getModel().put(GROUP, g);
        mav.getModel().put(CURRENT_GROUP_OBJECTS_SET, g.getStudents());
        mav.getModel().put(OBJECTS_SET, new HashSet<>(studentService.getStudentsSet()));
        mav.getModel().put(MESSAGE, msg);
        return mav;
    }

    private Group getGroupByIdWithStudents(int groupId) {
        Group g = groupService.getGroupById(groupId);
        log.debug("Вернули группу по groupId={} c параметрами {}", groupId, g);

        Set<Student> students = studentService.getStudentsByGroupId(groupId);
        g.setStudents(students);
        log.debug("Вернули Set students по groupId={} c параметрами {}", groupId, students);
        return g;
    }
}