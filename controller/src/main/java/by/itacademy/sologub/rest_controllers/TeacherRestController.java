package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.model.AbstractEntity;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@RestController
@RequestMapping("/rest/teachers")
public class TeacherRestController {
    private final TeacherService teacherService;

    @Autowired
    public TeacherRestController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public Set<Teacher> getAll() {
        return teacherService.getTeachersSet();
    }

    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable(ID) int id) {
        Teacher t = teacherService.getTeacherIfExistsOrGetSpecialValue(id);
        if (TEACHER_NOT_EXISTS == t) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher with id: " + id + " is not exists");
        }
        return t;
    }

    @PostMapping
    public Teacher addTeacherIfNotExists(@RequestBody Teacher newTeacher) {
        if (teacherService.putTeacherIfNotExists(newTeacher)) {
            Set<Teacher> teachers = teacherService.getTeachersSet();
            return Collections.max(teachers, Comparator.comparing(AbstractEntity::getId));
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public Teacher changeTeacher(@RequestBody Teacher newTeacher, @PathVariable int id) {
        Teacher oldTeacher = teacherService.getTeacherIfExistsOrGetSpecialValue(id);
        newTeacher.setId(id);
        if (teacherService.changeTeachersParametersIfExists(newTeacher)) {
            return teacherService.getTeacherIfExistsOrGetSpecialValue(id);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public Teacher deleteTeacher(@PathVariable int id) {
        Teacher oldTeacher = teacherService.getTeacherIfExistsOrGetSpecialValue(id);
        if (TEACHER_NOT_EXISTS == oldTeacher) {
            return null;
        } else {
            if (teacherService.deleteTeacher(oldTeacher)) {
                return oldTeacher;
            } else {
                return null;
            }
        }
    }
}