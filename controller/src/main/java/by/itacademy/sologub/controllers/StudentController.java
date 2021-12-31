package by.itacademy.sologub.controllers;

import by.itacademy.sologub.Credential;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.role.Role;
import by.itacademy.sologub.services.StudentService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Set;

import static by.itacademy.sologub.constants.Attributes.FIRSTNAME;
import static by.itacademy.sologub.constants.Attributes.ID;
import static by.itacademy.sologub.constants.Attributes.LASTNAME;
import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Attributes.PATRONYMIC;
import static by.itacademy.sologub.constants.Constant.ADMIN_STUDENTS_PAGE;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_ID;
import static by.itacademy.sologub.constants.Constant.DATE_OF_BIRTH;
import static by.itacademy.sologub.constants.Constant.STUDENT_CONTROLLER;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;

@WebServlet(STUDENT_CONTROLLER)
@Slf4j
public class StudentController extends AbstractPersonController<Student> {
    @Override
    protected String getRole() {
        return String.valueOf(Role.STUDENT);
    }

    @Override
    protected String getUrl() {
        return ADMIN_STUDENTS_PAGE;
    }

    @Override
    protected Set<Student> getSet() {
        StudentService service = (StudentService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.getStudentsSet();
    }

    @Override
    protected boolean putInRepo(Student user) {
        StudentService service = (StudentService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.putStudentIfNotExists(user);
    }

    @Override
    protected boolean changeInRepo(Student user) {
        StudentService service = (StudentService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.changeStudentParametersIfExists(user);
    }

    @Override
    protected boolean deleteInRepo(Student user) {
        StudentService service = (StudentService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.deleteStudent(user);
    }

    @Override
    protected boolean deleteInRepo(String userLogin) {
        StudentService service = (StudentService) getServletContext().getAttribute(FACADE_SERVICE);
        return service.deleteStudent(userLogin);
    }

    @Override
    protected Student extractUserFromForm(HttpServletRequest req) {
        Student s = new Student()
                .withCredential(new Credential()
                        .withLogin(req.getParameter(LOGIN))
                        .withPassword(req.getParameter(PASSWORD)))
                .withFirstname(req.getParameter(FIRSTNAME))
                .withLastname(req.getParameter(LASTNAME))
                .withPatronymic(req.getParameter(PATRONYMIC))
                .withDateOfBirth(LocalDate.parse(req.getParameter(DATE_OF_BIRTH)));

        log.debug("Из запроса извлечён обьект студента (без id и credential_id) {}", s);
        return s;
    }

    @Override
    protected Student extractUserFromFormWithIds(HttpServletRequest req) {
        Student s = extractUserFromForm(req);
        s.setId(Integer.parseInt(req.getParameter(ID)));
        s.getCredential().setId(Integer.parseInt(req.getParameter(CREDENTIAL_ID)));
        log.debug("Из запроса извлечён обьект студента {}", s);
        return s;
    }
}