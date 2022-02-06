package by.itacademy.sologub.rest_controllers;

import by.itacademy.sologub.model.Subject;
import by.itacademy.sologub.services.SubjectService;
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

import java.util.List;

import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.ConstantObject.SUBJECT_NOT_EXISTS;

@RequiredArgsConstructor
@RequestMapping("/rest/subjects")
@RestController
public class SubjectRestController {
    private final SubjectService subjectService;

    @GetMapping
    public List<Subject> getAll() {
        return subjectService.getSubjectsList();
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable(ID) int id) {
        Subject s = subjectService.getSubjectIfExistsOrGetSpecialValue(id);
        if (SUBJECT_NOT_EXISTS == s) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject with id: " + id + " is not exists");
        }
        return s;
    }

    @PostMapping
    public Subject addSubjectIfNotExists(@RequestBody Subject newSubject) {
        if (subjectService.putSubjectIfNotExists(newSubject)) {
            List<Subject> subjects = subjectService.getSubjectsList();
            return subjects.stream()
                    .filter(s -> newSubject.getTitle().equals(s.getTitle()))
                    .findAny().orElse(null);
        } else {
            return null;
        }
    }

    @PutMapping("/{id}")
    public Subject changeSubject(@RequestBody Subject newSubject, @PathVariable int id) {
        Subject oldSubject = subjectService.getSubjectIfExistsOrGetSpecialValue(id);
        newSubject.setId(id);
        if (subjectService.changeSubjectsParametersIfExists(newSubject)) {
            return oldSubject;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public Subject deleteSubject(@PathVariable int id) {
        Subject oldSubject = subjectService.getSubjectIfExistsOrGetSpecialValue(id);
        if (SUBJECT_NOT_EXISTS == oldSubject) {
            return null;
        } else {
            if (subjectService.deleteSubject(oldSubject)) {
                return oldSubject;
            } else {
                return null;
            }
        }
    }
}