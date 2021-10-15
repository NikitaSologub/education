package by.itacademy.sologub.filters;

import by.itacademy.sologub.*;
import by.itacademy.sologub.factory.ModelRepoFactory;
import by.itacademy.sologub.factory.ModelRepoFactoryHardcodeImpl;
import by.itacademy.sologub.role.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static by.itacademy.sologub.constants.Constant.*;

@WebFilter
public class InitContextFilter implements Filter {
    @Override        //Тут я буду инициализировать все что можно
    public void init(FilterConfig filterConfig) {
        ModelRepoFactory factory = ModelRepoFactoryHardcodeImpl.getInstance();

        SalariesRepo salariesRepo = factory.getSalariesRepo();
        CredentialRepo credentialRepo = factory.getCredentialRepo();
        TeacherRepo teacherRepo = factory.getTeacherRepo();
        StudentRepo studentRepo = factory.getStudentRepo();


        ServletContext context = filterConfig.getServletContext();

        setStudents(studentRepo);
        setTeachers(teacherRepo);

        context.setAttribute(SALARIES_REPO, salariesRepo);
        context.setAttribute(CREDENTIAL_REPO, credentialRepo);
        context.setAttribute(TEACHER_REPO, teacherRepo);
        context.setAttribute(STUDENT_REPO, studentRepo);
    }

    void setTeachers(TeacherRepo repo) {
        Credential cr1 = new Credential();
        cr1.setLogin("VIK_k");
        cr1.setPassword("teach12");
        Teacher t1 = new Teacher();
        t1.setCredential(cr1);
        t1.setFirstname("Валерия");
        t1.setPatronymic("Леонидовна");
        t1.setLastname("Грузинова");
        t1.setDateOfBirth(LocalDate.of(1974, Month.SEPTEMBER, 21));
        t1.setRole(Role.TEACHER);

        Credential cr2 = new Credential();
        cr2.setLogin("DETSUK59");
        cr2.setPassword("teacher4students");
        Teacher t2 = new Teacher();
        t2.setCredential(cr2);
        t2.setFirstname("Валерия");
        t2.setPatronymic("Сергеевна");
        t2.setLastname("Дэцук");
        t2.setDateOfBirth(LocalDate.of(1959, Month.OCTOBER, 12));
        t2.setRole(Role.TEACHER);

        Credential cr3 = new Credential();
        cr3.setLogin("arti");
        cr3.setPassword("soprofan");
        Teacher t3 = new Teacher();
        t3.setCredential(cr3);
        t3.setFirstname("Артур");
        t3.setPatronymic("Владимирович");
        t3.setLastname("Путято");
        t3.setDateOfBirth(LocalDate.of(1976, Month.FEBRUARY, 7));
        t3.setRole(Role.TEACHER);

        Credential cr4 = new Credential();
        cr4.setLogin("TVOROGGG");
        cr4.setPassword("great123");
        Teacher t4 = new Teacher();
        t4.setCredential(cr4);
        t4.setFirstname("Сергей");
        t4.setPatronymic("Петрович");
        t4.setLastname("Творогов");
        t4.setDateOfBirth(LocalDate.of(1957, Month.DECEMBER, 11));
        t4.setRole(Role.TEACHER);

        repo.putTeacherIfNotExists(t1);
        repo.putTeacherIfNotExists(t2);
        repo.putTeacherIfNotExists(t3);
        repo.putTeacherIfNotExists(t4);
    }

    void setStudents(StudentRepo repo) {
        Credential cr1 = new Credential();
        cr1.setLogin("AXEL23");
        cr1.setPassword("loveshortpasswords");
        Student s1 = new Student();
        s1.setCredential(cr1);
        s1.setFirstname("Илья");
        s1.setPatronymic("Викторович");
        s1.setLastname("Ярец");
        s1.setDateOfBirth(LocalDate.of(1995, Month.JULY, 22));
        s1.setRole(Role.STUDENT);

        Credential cr2 = new Credential();
        cr2.setLogin("STOLYAR55");
        cr2.setPassword("st678");
        Student s2 = new Student();
        s2.setCredential(cr2);
        s2.setFirstname("Анастасия");
        s2.setPatronymic("Ивановна");
        s2.setLastname("Столярчук");
        s2.setDateOfBirth(LocalDate.of(1995, Month.AUGUST, 17));
        s2.setRole(Role.STUDENT);

        Credential cr3 = new Credential();
        cr3.setLogin("BabkaVKedah");
        cr3.setPassword("worldoftanks");
        Student s3 = new Student();
        s3.setCredential(cr3);
        s3.setFirstname("Егор");
        s3.setPatronymic("Антонович");
        s3.setLastname("Татур");
        s3.setDateOfBirth(LocalDate.of(1995, Month.NOVEMBER, 27));
        s3.setRole(Role.STUDENT);

        Credential cr4 = new Credential();
        cr4.setLogin("Smartdyika");
        cr4.setPassword("books34");
        Student s4 = new Student();
        s4.setCredential(cr4);
        s4.setFirstname("Ксения");
        s4.setPatronymic("Антонович");
        s4.setLastname("Полошавец");
        s4.setDateOfBirth(LocalDate.of(1995, Month.APRIL, 16));
        s4.setRole(Role.STUDENT);

        Credential cr5 = new Credential();
        cr5.setLogin("azazello666");
        cr5.setPassword("tech1");
        Student s5 = new Student();
        s5.setCredential(cr5);
        s5.setFirstname("Андрей");
        s5.setPatronymic("Сергеевич");
        s5.setLastname("Анпилов");
        s5.setDateOfBirth(LocalDate.of(1991, Month.DECEMBER, 11));
        s5.setRole(Role.STUDENT);

        Credential cr6 = new Credential();
        cr6.setLogin("Shabalina_Anzhela");
        cr6.setPassword("flower4");
        Student s6 = new Student();
        s6.setCredential(cr6);
        s6.setFirstname("Анжелина");
        s6.setPatronymic("Игоревна");
        s6.setLastname("Шабалина");
        s6.setDateOfBirth(LocalDate.of(1995, Month.MARCH, 28));
        s6.setRole(Role.STUDENT);

        repo.putStudentIfNotExists(s1);
        repo.putStudentIfNotExists(s2);
        repo.putStudentIfNotExists(s3);
        repo.putStudentIfNotExists(s4);
        repo.putStudentIfNotExists(s5);
        repo.putStudentIfNotExists(s6);
    }

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain c) throws ServletException, IOException {
        c.doFilter(rq, rs);
    }
}