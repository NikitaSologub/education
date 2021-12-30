package by.itacademy.sologub.filters;

import by.itacademy.sologub.Admin;
import by.itacademy.sologub.AdminRepo;
import by.itacademy.sologub.Credential;
import by.itacademy.sologub.CredentialRepo;
import by.itacademy.sologub.Group;
import by.itacademy.sologub.GroupRepo;
import by.itacademy.sologub.Mark;
import by.itacademy.sologub.MarkRepo;
import by.itacademy.sologub.Salary;
import by.itacademy.sologub.SalaryRepo;
import by.itacademy.sologub.Student;
import by.itacademy.sologub.StudentRepo;
import by.itacademy.sologub.Subject;
import by.itacademy.sologub.SubjectRepo;
import by.itacademy.sologub.Teacher;
import by.itacademy.sologub.TeacherRepo;
import by.itacademy.sologub.factory.ModelRepoFactory;
import by.itacademy.sologub.factory.ModelRepoFactoryHardcodeImpl;
import by.itacademy.sologub.factory.ModelRepoFactoryHibernateImpl;
import by.itacademy.sologub.factory.ModelRepoFactoryPostgresDbImpl;
import by.itacademy.sologub.services.AdminService;
import by.itacademy.sologub.services.AdminServiceImpl;
import by.itacademy.sologub.services.FacadeService;
import by.itacademy.sologub.services.GroupService;
import by.itacademy.sologub.services.GroupServiceImpl;
import by.itacademy.sologub.services.MarkService;
import by.itacademy.sologub.services.MarkServiceImpl;
import by.itacademy.sologub.services.SalaryService;
import by.itacademy.sologub.services.SalaryServiceImpl;
import by.itacademy.sologub.services.StudentService;
import by.itacademy.sologub.services.StudentServiceImpl;
import by.itacademy.sologub.services.SubjectService;
import by.itacademy.sologub.services.SubjectServiceImpl;
import by.itacademy.sologub.services.TeacherService;
import by.itacademy.sologub.services.TeacherServiceImpl;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static by.itacademy.sologub.constants.Attributes.LOGIN;
import static by.itacademy.sologub.constants.Attributes.PASSWORD;
import static by.itacademy.sologub.constants.Constant.ADMIN_REPO;
import static by.itacademy.sologub.constants.Constant.CREDENTIAL_REPO;
import static by.itacademy.sologub.constants.Constant.DB_CONFIG_FILE;
import static by.itacademy.sologub.constants.Constant.DRIVER;
import static by.itacademy.sologub.constants.Constant.FACADE_SERVICE;
import static by.itacademy.sologub.constants.Constant.GROUP_REPO;
import static by.itacademy.sologub.constants.Constant.HIBERNATE;
import static by.itacademy.sologub.constants.Constant.MARK_REPO;
import static by.itacademy.sologub.constants.Constant.MEMORY;
import static by.itacademy.sologub.constants.Constant.POSTGRES;
import static by.itacademy.sologub.constants.Constant.SALARY_REPO;
import static by.itacademy.sologub.constants.Constant.STUDENT_REPO;
import static by.itacademy.sologub.constants.Constant.SUBJECT_REPO;
import static by.itacademy.sologub.constants.Constant.TEACHER_REPO;
import static by.itacademy.sologub.constants.Constant.TYPE;
import static by.itacademy.sologub.constants.Constant.URL;

@WebFilter
@Slf4j
public class InitContextFilter implements Filter {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_CONFIG_FILE);
    ComboPooledDataSource pool = null;

    @SneakyThrows
    @Override        //Тут я буду инициализировать все что можно
    public void init(FilterConfig filterConfig) {
        String databaseType = resourceBundle.getString(TYPE);

        initRepositoryAndSetContext(databaseType, filterConfig);
    }

    private void initRepositoryAndSetContext(String databaseType, FilterConfig filterConfig) throws PropertyVetoException {
        log.info("Приложение должно загружать {} тип баз данных", databaseType);
        switch (databaseType) {
            case (POSTGRES):
                loadDatabasePostgres(filterConfig);
                break;
            case (HIBERNATE):
                loadDatabaseHibernateApproach(filterConfig);
                break;
            case (MEMORY):
            default:
                loadDatabaseInMemory(filterConfig);
                break;
        }
    }

    private void loadDatabaseInMemory(FilterConfig conf) {
        ModelRepoFactory factory = ModelRepoFactoryHardcodeImpl.getInstance();
        setAppContext(conf, factory);
        setContent(factory);
        log.info("Не получилось подключиться к БД. Переходим на HardcoreMemoryImpl Database");
    }

    private void loadDatabaseHibernateApproach(FilterConfig conf) {
        log.info("Пытаемся загружать hibernate configuration");//TODO если нет - переход на memory type
        Configuration cfg = new Configuration().configure();
        try {
            SessionFactory sf = cfg.buildSessionFactory();
            ModelRepoFactory factory = ModelRepoFactoryHibernateImpl.getInstance(sf);
            setAppContext(conf, factory);
        } catch (HibernateException e) {
            log.info("Не получилось сконфигурировать hibernate. Переходим на HardcoreMemoryImpl Database");
            loadDatabaseInMemory(conf);
        }
    }

    private void loadDatabasePostgres(FilterConfig conf) throws PropertyVetoException {
        if (loadDriverClass()) {
            pool = initAndGetPoolConnection();
            ModelRepoFactory factory = ModelRepoFactoryPostgresDbImpl.getInstance(pool);
            setAppContext(conf, factory);
        } else {
            log.info("Не получилось подключиться к БД. Переходим на HardcoreMemoryImpl Database");
            loadDatabaseInMemory(conf);
        }
    }

    private void setAppContext(FilterConfig filterConfig, ModelRepoFactory factory) {
        CredentialRepo credentialRepo = factory.getCredentialRepo();
        TeacherRepo teacherRepo = factory.getTeacherRepo();
        StudentRepo studentRepo = factory.getStudentRepo();
        AdminRepo adminRepo = factory.getAdminRepo();
        SalaryRepo salaryRepo = factory.getSalariesRepo();
        SubjectRepo subjectRepo = factory.getSubjectRepo();
        GroupRepo groupRepo = factory.getGroupRepo();
        MarkRepo markRepo = factory.getMarkRepo();

        TeacherService teacherService = TeacherServiceImpl.getInstance(teacherRepo);
        StudentService studentService = StudentServiceImpl.getInstance(studentRepo);
        GroupService groupService = GroupServiceImpl.getInstance(groupRepo);
        MarkService markService = MarkServiceImpl.getInstance(markRepo);
        SalaryService salaryService = SalaryServiceImpl.getInstance(salaryRepo);
        SubjectService subjectService = SubjectServiceImpl.getInstance(subjectRepo);
        AdminService adminService = AdminServiceImpl.getInstance(adminRepo);
        FacadeService facade = FacadeService.getInstance(groupService, studentService, teacherService, adminService,
                markService, salaryService, subjectService);

        ServletContext context = filterConfig.getServletContext();
        context.setAttribute(CREDENTIAL_REPO, credentialRepo);
        context.setAttribute(TEACHER_REPO, teacherRepo);
        context.setAttribute(STUDENT_REPO, studentRepo);
        context.setAttribute(ADMIN_REPO, adminRepo);
        context.setAttribute(SALARY_REPO, salaryRepo);
        context.setAttribute(SUBJECT_REPO, subjectRepo);
        context.setAttribute(GROUP_REPO, groupRepo);
        context.setAttribute(MARK_REPO, markRepo);
        context.setAttribute(FACADE_SERVICE, facade);
    }

    boolean loadDriverClass() {
        try {
            Class.forName(resourceBundle.getString(DRIVER));
            log.info("Драйвер {} загружен", resourceBundle.getString(DRIVER));
            return true;
        } catch (ClassNotFoundException e) {
            log.error("не удалость подключить драйвер.", e);
            return false;
        }
    }

    private ComboPooledDataSource initAndGetPoolConnection() throws PropertyVetoException {
        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setJdbcUrl(resourceBundle.getString(URL));
        pool.setUser(resourceBundle.getString(LOGIN));
        pool.setPassword(resourceBundle.getString(PASSWORD));
        pool.setInitialPoolSize(1);
        pool.setMinPoolSize(1);
        pool.setAcquireIncrement(1);
        pool.setMaxPoolSize(5);
        pool.setMaxStatements(100);
        pool.setDriverClass(resourceBundle.getString(DRIVER));
        return pool;
    }

    private void setContent(ModelRepoFactory factory) {
        SubjectRepo subjectRepo = factory.getSubjectRepo();

        Subject sub1 = new Subject().withTitle("География");
        Subject sub2 = new Subject().withTitle("Экономика");
        Subject sub3 = new Subject().withTitle("Информатика");
        Subject sub4 = new Subject().withTitle("Русский");
        Subject sub5 = new Subject().withTitle("Белорусский");
        Subject sub6 = new Subject().withTitle("Природоведение");

        subjectRepo.putSubjectIfNotExists(sub1);
        subjectRepo.putSubjectIfNotExists(sub2);
        subjectRepo.putSubjectIfNotExists(sub3);
        subjectRepo.putSubjectIfNotExists(sub4);
        subjectRepo.putSubjectIfNotExists(sub5);
        subjectRepo.putSubjectIfNotExists(sub6);

        AdminRepo adminRepo = factory.getAdminRepo();

        adminRepo.putAdminIfNotExists(new Admin()
                .withId(323)
                .withCredential(new Credential()
                        .withId(0)
                        .withLogin("ADMIN")
                        .withPassword("234"))
                .withFirstname("Никита")
                .withLastname("Сологуб")
                .withPatronymic("Олегович")
                .withDateOfBirth(LocalDate.of(1992, Month.APRIL, 23)));

        StudentRepo studentRepo = factory.getStudentRepo();

        Student stud1 = new Student()
                .withCredential(new Credential()
                        .withLogin("AXEL23")
                        .withPassword("123"))
                .withLastname("Ярец")
                .withFirstname("Илья")
                .withPatronymic("Викторович")
                .withDateOfBirth(LocalDate.of(1995, Month.JULY, 22));

        Student stud2 = new Student()
                .withCredential(new Credential()
                        .withLogin("STOLYAR55")
                        .withPassword("st678"))
                .withLastname("Столярчук")
                .withFirstname("Анастасия")
                .withPatronymic("Ивановна")
                .withDateOfBirth(LocalDate.of(1995, Month.AUGUST, 17));

        Student stud3 = new Student()
                .withCredential(new Credential()
                        .withLogin("BabkaVKedah")
                        .withPassword("worldoftanks"))
                .withLastname("Татур")
                .withFirstname("Егор")
                .withPatronymic("Евгеньевич")
                .withDateOfBirth(LocalDate.of(1995, Month.NOVEMBER, 27));

        Student stud4 = new Student()
                .withCredential(new Credential()
                        .withLogin("Smartdyika")
                        .withPassword("books34"))
                .withLastname("Полошавец")
                .withFirstname("Ксения")
                .withPatronymic("Антоновна")
                .withDateOfBirth(LocalDate.of(1995, Month.APRIL, 10));

        Student stud5 = new Student()
                .withCredential(new Credential()
                        .withLogin("azazello666")
                        .withPassword("tech1"))
                .withLastname("Анпилов")
                .withFirstname("Андрей")
                .withPatronymic("Сергеевич")
                .withDateOfBirth(LocalDate.of(1991, Month.DECEMBER, 11));

        Student stud6 = new Student()
                .withCredential(new Credential()
                        .withLogin("Shabalina_Anzhela")
                        .withPassword("flower4"))
                .withLastname("Шабалина")
                .withFirstname("Анжелина")
                .withPatronymic("Игоревна")
                .withDateOfBirth(LocalDate.of(1995, Month.MARCH, 28));

        studentRepo.putStudentIfNotExists(stud1);
        studentRepo.putStudentIfNotExists(stud2);
        studentRepo.putStudentIfNotExists(stud3);
        studentRepo.putStudentIfNotExists(stud4);
        studentRepo.putStudentIfNotExists(stud5);
        studentRepo.putStudentIfNotExists(stud6);

        TeacherRepo teacherRepo = factory.getTeacherRepo();

        Teacher t1 = new Teacher().
                withCredential(new Credential()
                        .withLogin("DETSUK59")
                        .withPassword("ekology"))
                .withLastname("Дэцук")
                .withFirstname("Валерия")
                .withPatronymic("Сергеевна")
                .withDateOfBirth(LocalDate.of(1974, Month.SEPTEMBER, 21));

        Teacher t2 = new Teacher().
                withCredential(new Credential()
                        .withLogin("VIK_k")
                        .withPassword("teach12"))
                .withLastname("Грузинова")
                .withFirstname("Валерия")
                .withPatronymic("Леонидовна")
                .withDateOfBirth(LocalDate.of(1959, Month.OCTOBER, 12));

        Teacher t3 = new Teacher().
                withCredential(new Credential()
                        .withLogin("arti")
                        .withPassword("soprofan"))
                .withLastname("Путято")
                .withFirstname("Артур")
                .withPatronymic("Владимирович")
                .withDateOfBirth(LocalDate.of(1976, Month.FEBRUARY, 7));

        Teacher t4 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Tvorog57")
                        .withPassword("great123"))
                .withLastname("Творогов")
                .withFirstname("Сергей")
                .withPatronymic("Петрович")
                .withDateOfBirth(LocalDate.of(1957, Month.DECEMBER, 11));

        Teacher t5 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Med1")
                        .withPassword("123"))
                .withLastname("Мединский")
                .withFirstname("Ян")
                .withPatronymic("Станиславович")
                .withDateOfBirth(LocalDate.of(1971, Month.NOVEMBER, 21));

        teacherRepo.putTeacherIfNotExists(t1);
        teacherRepo.putTeacherIfNotExists(t2);
        teacherRepo.putTeacherIfNotExists(t3);
        teacherRepo.putTeacherIfNotExists(t4);
        teacherRepo.putTeacherIfNotExists(t5);

        SalaryRepo salaryRepo = factory.getSalariesRepo();

        Salary s1 = new Salary()
                .withDate(LocalDate.parse("2021-01-12"))
                .withCoins(64140);
        Salary s2 = new Salary()
                .withDate(LocalDate.parse("2021-02-11"))
                .withCoins(63716);
        Salary s3 = new Salary()
                .withDate(LocalDate.parse("2021-03-13"))
                .withCoins(61898);

        Salary s4 = new Salary()
                .withDate(LocalDate.parse("2021-04-12"))
                .withCoins(64140);
        Salary s5 = new Salary()
                .withDate(LocalDate.parse("2021-05-11"))
                .withCoins(63716);
        Salary s6 = new Salary()
                .withDate(LocalDate.parse("2021-06-21"))
                .withCoins(61898);

        Salary s7 = new Salary()
                .withDate(LocalDate.parse("2021-07-16"))
                .withCoins(64140);
        Salary s8 = new Salary()
                .withDate(LocalDate.parse("2021-08-14"))
                .withCoins(63716);
        Salary s9 = new Salary()
                .withDate(LocalDate.parse("2021-09-11"))
                .withCoins(61898);

        Salary s10 = new Salary()
                .withDate(LocalDate.parse("2021-10-14"))
                .withCoins(64140);
        Salary s11 = new Salary()
                .withDate(LocalDate.parse("2021-11-16"))
                .withCoins(63716);
        Salary s12 = new Salary()
                .withDate(LocalDate.parse("2021-12-15"))
                .withCoins(61898);

        Salary s13 = new Salary()
                .withDate(LocalDate.parse("2022-01-17"))
                .withCoins(65085);

        salaryRepo.putSalaryToTeacher(s1, t1.getId());
        salaryRepo.putSalaryToTeacher(s2, t1.getId());
        salaryRepo.putSalaryToTeacher(s3, t1.getId());
        salaryRepo.putSalaryToTeacher(s4, t2.getId());
        salaryRepo.putSalaryToTeacher(s5, t2.getId());
        salaryRepo.putSalaryToTeacher(s6, t2.getId());
        salaryRepo.putSalaryToTeacher(s7, t3.getId());
        salaryRepo.putSalaryToTeacher(s8, t3.getId());
        salaryRepo.putSalaryToTeacher(s9, t3.getId());
        salaryRepo.putSalaryToTeacher(s10, t4.getId());
        salaryRepo.putSalaryToTeacher(s11, t4.getId());
        salaryRepo.putSalaryToTeacher(s12, t4.getId());
        salaryRepo.putSalaryToTeacher(s13, t5.getId());

        MarkRepo markRepo = factory.getMarkRepo();

        Mark m1 = new Mark()
                .withSubject(sub1)
                .withPoint(90)
                .withDate(LocalDate.parse("2021-09-11"));
        Mark m2 = new Mark()
                .withSubject(sub2)
                .withPoint(55)
                .withDate(LocalDate.parse("2021-10-21"));
        Mark m3 = new Mark()
                .withSubject(sub3)
                .withPoint(80)
                .withDate(LocalDate.parse("2021-11-04"));
        Mark m4 = new Mark()
                .withSubject(sub4)
                .withPoint(98)
                .withDate(LocalDate.parse("2021-12-07"));
        Mark m5 = new Mark()
                .withSubject(sub5)
                .withPoint(12)
                .withDate(LocalDate.parse("2021-05-16"));
        Mark m6 = new Mark()
                .withSubject(sub6)
                .withPoint(43)
                .withDate(LocalDate.parse("2021-06-18"));
        Mark m7 = new Mark()
                .withSubject(sub1)
                .withPoint(56)
                .withDate(LocalDate.parse("2021-07-12"));
        Mark m8 = new Mark()
                .withSubject(sub2)
                .withPoint(57)
                .withDate(LocalDate.parse("2021-04-14"));
        Mark m9 = new Mark()
                .withSubject(sub3)
                .withPoint(14)
                .withDate(LocalDate.parse("2021-03-13"));
        Mark m10 = new Mark()
                .withSubject(sub4)
                .withPoint(51)
                .withDate(LocalDate.parse("2021-06-18"));
        Mark m11 = new Mark()
                .withSubject(sub5)
                .withPoint(76)
                .withDate(LocalDate.parse("2021-08-28"));
        Mark m12 = new Mark()
                .withSubject(sub6)
                .withPoint(22)
                .withDate(LocalDate.parse("2021-02-26"));

        markRepo.putMarkToStudent(m1, stud1.getId());
        markRepo.putMarkToStudent(m2, stud1.getId());
        markRepo.putMarkToStudent(m3, stud2.getId());
        markRepo.putMarkToStudent(m4, stud2.getId());
        markRepo.putMarkToStudent(m5, stud3.getId());
        markRepo.putMarkToStudent(m6, stud3.getId());
        markRepo.putMarkToStudent(m7, stud4.getId());
        markRepo.putMarkToStudent(m8, stud4.getId());
        markRepo.putMarkToStudent(m9, stud5.getId());
        markRepo.putMarkToStudent(m10, stud5.getId());
        markRepo.putMarkToStudent(m11, stud6.getId());
        markRepo.putMarkToStudent(m12, stud6.getId());

        GroupRepo groupRepo = factory.getGroupRepo();

        Group gr1 = new Group()
                .withTitle("Смешарики")
                .withTeacher(t1)
                .withDescription("Группа для весёлого изучения любых дисциплин")
                .withStudents(stud1, stud2, stud3, stud4, stud5, stud6)
                .withSubjects(sub5, sub4);

        Group gr2 = new Group()
                .withTitle("Болванчики")
                .withTeacher(t2)
                .withDescription("Группа для хулиганов (из СПТУ)")
                .withStudents(stud4, stud5)
                .withSubjects(sub5, sub4);

        Group gr3 = new Group()
                .withTitle("Умники")
                .withTeacher(t3)
                .withDescription("Группа для самых умных")
                .withStudents(stud1, stud2, stud3, stud6)
                .withSubjects(sub5, sub4);

        Group gr4 = new Group()
                .withTitle("Тяп Ляп");

        groupRepo.putGroupIfNotExists(gr1);
        groupRepo.putGroupIfNotExists(gr2);
        groupRepo.putGroupIfNotExists(gr3);
        groupRepo.putGroupIfNotExists(gr4);
    }

    @Override
    public void doFilter(ServletRequest rq, ServletResponse rs, FilterChain c) throws ServletException, IOException {
        c.doFilter(rq, rs);
    }

    @Override
    public void destroy() {
        if (pool != null) {
            pool.close();
            log.info("Закрываем pool connection");
        }
    }
}