package by.itacademy.sologub.controllers;

import by.itacademy.sologub.model.Mark;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.services.MarkService;
import by.itacademy.sologub.services.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.STUDENT;
import static by.itacademy.sologub.constants.Constant.OBJECTS_SET;
import static by.itacademy.sologub.constants.Constant.STUDENT_ALL_MARKS_VIEW;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MarkControllerForStudent {
    private final MarkService markService;
    private final StudentService studentService;

    @GetMapping("marks")
    public ModelAndView getAllMarks(Principal principal) {
        Student student = studentService.getStudentIfExistsOrGetSpecialValue(principal.getName());
        Set<Mark> markSet = markService.getAllMarksByStudentId(student.getId());
        ModelAndView mav = new ModelAndView(STUDENT_ALL_MARKS_VIEW);
        mav.getModel().put(STUDENT, student);
        mav.getModel().put(OBJECTS_SET, markSet);
        return mav;
    }
}