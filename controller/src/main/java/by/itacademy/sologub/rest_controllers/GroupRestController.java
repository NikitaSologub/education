package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.model.AbstractEntity;
import by.itacademy.sologub.model.Group;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.services.GroupService;
import by.itacademy.sologub.services.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static by.itacademy.sologub.constants.Constant.GROUP_ID;
import static by.itacademy.sologub.constants.Constant.STUDENT_ID;
import static by.itacademy.sologub.constants.ConstantObject.GROUP_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@RequiredArgsConstructor
@RequestMapping("/rest/groups")
@RestController
public class GroupRestController {
    private final GroupService groupService;
    private final StudentService studentService;

    @GetMapping
    public List<Group> getAll() {
        return groupService.getGroups();
    }

    @GetMapping("/{groupId}")
    public Group getGroup(@PathVariable(GROUP_ID) int groupId) {
        Group g = groupService.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == g) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group with id: " + groupId + " is not exists");
        }
        return g;
    }

    @PostMapping
    public Group addGroup(@RequestBody Group newGroup) {
        if (groupService.putGroupIfNotExists(newGroup)) {
            List<Group> groups = groupService.getGroups();
            return Collections.max(groups, Comparator.comparing(AbstractEntity::getId));
        } else {
            return null;
        }

    }

    @PostMapping("/{groupId}/students/{studentId}")
    public boolean includeStudentToGroup(@PathVariable(GROUP_ID) int groupId,
                                         @PathVariable(STUDENT_ID) int studentId) {
        Group g = groupService.getGroupById(groupId);
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentId);
        if (GROUP_NOT_EXISTS == g || STUDENT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Group with id: " + groupId + "or Student with id:" + studentId + " is not exists");
        }
        return groupService.addStudentInGroup(g, s);
    }

    @PutMapping("/{groupId}")
    public Group changeGroup(@RequestBody Group newGroup, @PathVariable(GROUP_ID) int groupId) {
        Group oldGroup = groupService.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == oldGroup) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Group with id: " + groupId + " is not exists");
        }
        newGroup.setId(groupId);
        if (groupService.changeGroupsParametersIfExists(newGroup)) {
            return oldGroup;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{groupId}")
    public Group deleteGroup(@PathVariable(GROUP_ID) int groupId) {
        Group oldGroup = groupService.getGroupById(groupId);
        if (GROUP_NOT_EXISTS == oldGroup) {
            return null;
        } else {
            if (groupService.deleteGroupIfExists(groupId)) {
                return oldGroup;
            } else {
                return null;
            }
        }
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public boolean excludeStudentFromGroup(@PathVariable(GROUP_ID) int groupId,
                                           @PathVariable(STUDENT_ID) int studentId) {
        Group g = groupService.getGroupById(groupId);
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentId);
        if (GROUP_NOT_EXISTS == g || STUDENT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Group with id: " + groupId + "or Student with id:" + studentId + " is not exists");
        }
        return groupService.removeStudentFromGroup(g, s);
    }
}