package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.model.AbstractEntity;
import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.services.MarkService;
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
import java.util.Set;

import static by.itacademy.sologub.constants.Constant.MARK_ID;
import static by.itacademy.sologub.constants.Constant.STUDENT_ID;
import static by.itacademy.sologub.constants.ConstantObject.MARK_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@RequiredArgsConstructor
@RequestMapping("/rest/students/{studentId}/marks")
@RestController
public class MarkRestController {
    private final MarkService markService;
    private final StudentService studentService;

    @GetMapping
    public Set<Mark> getMarksByStudentId(@PathVariable(STUDENT_ID) int id) {
        return markService.getAllMarksByStudentId(id);
    }

    @GetMapping("/{markId}")
    public Mark getMarkById(@PathVariable(MARK_ID) int markId, @PathVariable(STUDENT_ID) int studentId) {
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentId);
        Mark m = markService.getMark(markId);
        if (STUDENT_NOT_EXISTS == s || MARK_NOT_EXISTS == m) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Student with id: " + studentId + "or Mark with id:" + markId + " is not exists");
        }
        return m;
    }

    @PostMapping
    public Mark addMarkIfNotExists(@RequestBody Mark newMark, @PathVariable(STUDENT_ID) int studentId) {
        if (markService.putMarkToStudent(newMark, studentId)) {
            Set<Mark> marks = markService.getAllMarksByStudentId(studentId);
            return Collections.max(marks, Comparator.comparing(AbstractEntity::getId));
        } else {
            return null;
        }
    }

    @PutMapping("/{markId}")
    public Mark changeMark(@PathVariable(MARK_ID) int markId, @PathVariable(STUDENT_ID) int studentId,
                           @RequestBody Mark newMark) {
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentId);
        Mark oldMark = markService.getMark(markId);
        if (MARK_NOT_EXISTS == oldMark || STUDENT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Student with id: " + studentId + "or Mark with id:" + markId + " is not exists");
        }
        newMark.setId(markId);
        if (markService.changeMark(newMark)) {
            return markService.getMark(markId);
        } else {
            return null;
        }
    }

    @DeleteMapping("/{markId}")
    public Mark deleteMark(@PathVariable(MARK_ID) int markId, @PathVariable(STUDENT_ID) int studentId) {
        Mark oldMark = markService.getMark(markId);
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(studentId);
        if (MARK_NOT_EXISTS == oldMark || STUDENT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Student with id: " + studentId + "or Mark with id:" + markId + " is not exists");
        }
        if (markService.deleteMark(markId)) {
            return oldMark;
        } else {
            return null;
        }
    }
}