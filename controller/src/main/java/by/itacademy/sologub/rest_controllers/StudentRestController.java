package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.model.AbstractEntity;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.services.StudentService;
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
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;

@RestController
@RequestMapping("/rest/students")
public class StudentRestController {
    private final StudentService studentService;

    @Autowired
    public StudentRestController(StudentService subjectService) {
        this.studentService = subjectService;
    }

    @GetMapping
    public Set<Student> getAll() {
        return studentService.getStudentsSet();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable(ID) int id) {
        Student s = studentService.getStudentIfExistsOrGetSpecialValue(id);
        if (STUDENT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student with id: " + id + " is not exists");
        }
        return s;
    }

    @PostMapping
    public Student addStudentIfNotExists(@RequestBody Student newStudent) {
        if (studentService.putStudentIfNotExists(newStudent)) {
            Set<Student> students = studentService.getStudentsSet();
            return Collections.max(students, Comparator.comparing(AbstractEntity::getId));
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public Student changeStudent(@RequestBody Student newStudent, @PathVariable int id) {
        Student oldStudent = studentService.getStudentIfExistsOrGetSpecialValue(id);
        newStudent.setId(id);
        if (studentService.changeStudentParametersIfExists(newStudent)) {
            return oldStudent;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public Student deleteStudent(@PathVariable int id) {
        Student oldStudent = studentService.getStudentIfExistsOrGetSpecialValue(id);
        if (STUDENT_NOT_EXISTS == oldStudent) {
            return null;
        } else {
            if (studentService.deleteStudent(oldStudent)) {
                return oldStudent;
            } else {
                return null;
            }
        }
    }
}