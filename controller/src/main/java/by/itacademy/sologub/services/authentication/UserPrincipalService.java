package by.itacademy.sologub.services.authentication;

import by.itacademy.sologub.model.Admin;
import by.itacademy.sologub.model.Student;
import by.itacademy.sologub.model.Teacher;
import by.itacademy.sologub.model.User;
import by.itacademy.sologub.services.AbstractService;
import by.itacademy.sologub.services.AdminService;
import by.itacademy.sologub.services.StudentService;
import by.itacademy.sologub.services.TeacherService;
import by.itacademy.sologub.services.authentication.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static by.itacademy.sologub.constants.ConstantObject.ADMIN_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.STUDENT_NOT_EXISTS;
import static by.itacademy.sologub.constants.ConstantObject.TEACHER_NOT_EXISTS;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserPrincipalService extends AbstractService implements UserDetailsService {
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional = getUserFromDb(s);
        User user = userOptional.orElseThrow(() -> {
            throw new UsernameNotFoundException(String.format("User %s not found", s));
        });
        UserPrincipal uP = new UserPrincipal(user);
        log.info("Возвращаем UserPrincipal {} c Authorities={}", uP.getUsername(), uP.getAuthorities());
        return uP;
    }

    @Transactional(readOnly = true)
    Optional<User> getUserFromDb(String login) {
        Teacher teacher = teacherService.getTeacherIfExistsOrGetSpecialValue(login);
        log.info("Взяли из репозитория Teacher={}", teacher.getCredential().getLogin());
        Student student = studentService.getStudentIfExistsOrGetSpecialValue(login);
        log.info("Взяли из репозитория Student={}", student.getCredential().getLogin());
        Admin admin = adminService.getAdminIfExistsOrGetSpecialValue(login);
        log.info("Взяли из репозитория Admin={}", admin.getCredential().getLogin());

        if (TEACHER_NOT_EXISTS != teacher) return Optional.of(teacher);
        if (STUDENT_NOT_EXISTS != student) return Optional.of(student);
        if (ADMIN_NOT_EXISTS != admin) return Optional.of(admin);
        return Optional.empty();
    }
}