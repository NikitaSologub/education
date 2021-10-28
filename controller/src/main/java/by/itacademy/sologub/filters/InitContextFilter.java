package by.itacademy.sologub.filters;

import by.itacademy.sologub.*;
import by.itacademy.sologub.factory.ModelRepoFactory;
import by.itacademy.sologub.factory.ModelRepoFactoryHardcodeImpl;
import by.itacademy.sologub.role.Role;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static by.itacademy.sologub.constants.Constant.*;
import static by.itacademy.sologub.constants.Constants.LOGIN;
import static by.itacademy.sologub.constants.Constants.PASSWORD;

@WebFilter
@Slf4j
public class InitContextFilter implements Filter {
    ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_CONFIG_FILE);

    @SneakyThrows
    @Override        //Тут я буду инициализировать все что можно
    public void init(FilterConfig filterConfig) {
        String databaseType = resourceBundle.getString(TYPE);

        initRepositoryAndSetContext(databaseType, filterConfig);
    }

    private void initRepositoryAndSetContext(String databaseType, FilterConfig filterConfig) throws PropertyVetoException {
        log.info("Приложение должно загружать {} тип баз данных", databaseType);
        switch (databaseType) {
            case ("postgres"):
                loadDatabasePostgres(filterConfig);
                break;
            case ("memory"):
            default:
                loadDatabaseInMemory(filterConfig);
                break;
        }
    }

    private void loadDatabasePostgres(FilterConfig filterConfig) throws PropertyVetoException {
        if (loadDriverClass()) {
            ComboPooledDataSource pool = getPoolConnection();
            CredentialRepoPostgresImpl credentialRepo = CredentialRepoPostgresImpl.getInstance(pool);
            TeacherRepoPostgresImpl teacherRepo = TeacherRepoPostgresImpl.getInstance(pool, credentialRepo);

            ServletContext context = filterConfig.getServletContext();
            context.setAttribute(CREDENTIAL_REPO, credentialRepo);
            context.setAttribute(TEACHER_REPO, teacherRepo);
        } else {
            loadDatabaseInMemory(filterConfig);
            log.info("Не получилось подключиться к БД. Переходим на HardcoreMemoryImpl Database");
        }
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

    private ComboPooledDataSource getPoolConnection() throws PropertyVetoException {
        ComboPooledDataSource pool = new ComboPooledDataSource();
        pool.setJdbcUrl(resourceBundle.getString(URL));
        pool.setUser(resourceBundle.getString(LOGIN));
        pool.setPassword(resourceBundle.getString(PASSWORD));

        // Optional Settings
        pool.setInitialPoolSize(1);
        pool.setMinPoolSize(1);
        pool.setAcquireIncrement(1);
        pool.setMaxPoolSize(5);
        pool.setMaxStatements(100);
        pool.setDriverClass(resourceBundle.getString(DRIVER));
        return pool;
    }

    private void loadDatabaseInMemory(FilterConfig filterConfig) {
        ModelRepoFactory factory = ModelRepoFactoryHardcodeImpl.getInstance();

        SalariesRepo salariesRepo = factory.getSalariesRepo();
        CredentialRepo credentialRepo = factory.getCredentialRepo();
        TeacherRepo teacherRepo = factory.getTeacherRepo();
        StudentRepo studentRepo = factory.getStudentRepo();

        ServletContext context = filterConfig.getServletContext();

        setStudents(studentRepo);
        setTeachersAndSalaries(teacherRepo, salariesRepo);

        context.setAttribute(SALARIES_REPO, salariesRepo);
        context.setAttribute(CREDENTIAL_REPO, credentialRepo);
        context.setAttribute(TEACHER_REPO, teacherRepo);
        context.setAttribute(STUDENT_REPO, studentRepo);
    }

    void setTeachersAndSalaries(TeacherRepo teacherRepo, SalariesRepo salariesRepo) {
        Teacher t1 = new Teacher().
                withCredential(new Credential()
                        .withLogin("DETSUK59")
                        .withPassword("ekology"))
                .withLastname("Дэцук")
                .withFirstname("Валерия")
                .withPatronymic("Сергеевна")
                .withDateOfBirth(LocalDate.of(1974, Month.SEPTEMBER, 21))
                .withRole(Role.TEACHER);

        Teacher t2 = new Teacher().
                withCredential(new Credential()
                        .withLogin("VIK_k")
                        .withPassword("teach12"))
                .withLastname("Грузинова")
                .withFirstname("Валерия")
                .withPatronymic("Леонидовна")
                .withDateOfBirth(LocalDate.of(1959, Month.OCTOBER, 12))
                .withRole(Role.TEACHER);

        Teacher t3 = new Teacher().
                withCredential(new Credential()
                        .withLogin("arti")
                        .withPassword("soprofan"))
                .withLastname("Путято")
                .withFirstname("Артур")
                .withPatronymic("Владимирович")
                .withDateOfBirth(LocalDate.of(1976, Month.FEBRUARY, 7))
                .withRole(Role.TEACHER);

        Teacher t4 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Tvorog57")
                        .withPassword("great123"))
                .withLastname("Творогов")
                .withFirstname("Сергей")
                .withPatronymic("Петрович")
                .withDateOfBirth(LocalDate.of(1957, Month.DECEMBER, 11))
                .withRole(Role.TEACHER);

        Teacher t5 = new Teacher().
                withCredential(new Credential()
                        .withLogin("Med1")
                        .withPassword("123"))
                .withLastname("Мединский")
                .withFirstname("Ян")
                .withPatronymic("Станиславович")
                .withDateOfBirth(LocalDate.of(1971, Month.NOVEMBER, 21))
                .withRole(Role.TEACHER);

        teacherRepo.putTeacherIfNotExists(t1);
        teacherRepo.putTeacherIfNotExists(t2);
        teacherRepo.putTeacherIfNotExists(t3);
        teacherRepo.putTeacherIfNotExists(t4);
        teacherRepo.putTeacherIfNotExists(t5);

        Salary s1 = new Salary()
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-01-12"))
                .withCoins(64140);
        Salary s2 = new Salary()
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-02-11"))
                .withCoins(63716);
        Salary s3 = new Salary()
                .withTeacherId(t1.getId())
                .withDate(LocalDate.parse("2021-03-13"))
                .withCoins(61898);

        Salary s4 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-04-12"))
                .withCoins(64140);
        Salary s5 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-05-11"))
                .withCoins(63716);
        Salary s6 = new Salary()
                .withTeacherId(t2.getId())
                .withDate(LocalDate.parse("2021-06-21"))
                .withCoins(61898);

        Salary s7 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-07-16"))
                .withCoins(64140);
        Salary s8 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-08-14"))
                .withCoins(63716);
        Salary s9 = new Salary()
                .withTeacherId(t3.getId())
                .withDate(LocalDate.parse("2021-09-11"))
                .withCoins(61898);

        Salary s10 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-10-14"))
                .withCoins(64140);
        Salary s11 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-11-16"))
                .withCoins(63716);
        Salary s12 = new Salary()
                .withTeacherId(t4.getId())
                .withDate(LocalDate.parse("2021-12-15"))
                .withCoins(61898);

        Salary s13 = new Salary()
                .withTeacherId(t5.getId())
                .withDate(LocalDate.parse("2022-01-17"))
                .withCoins(65085);

        salariesRepo.putSalary(s1);
        salariesRepo.putSalary(s2);
        salariesRepo.putSalary(s3);
        salariesRepo.putSalary(s4);
        salariesRepo.putSalary(s5);
        salariesRepo.putSalary(s6);
        salariesRepo.putSalary(s7);
        salariesRepo.putSalary(s8);
        salariesRepo.putSalary(s9);
        salariesRepo.putSalary(s10);
        salariesRepo.putSalary(s11);
        salariesRepo.putSalary(s12);
        salariesRepo.putSalary(s13);
    }

    void setStudents(StudentRepo repo) {
        Student s1 = new Student()
                .withCredential(new Credential()
                        .withLogin("AXEL23")
                        .withPassword("loveshortpasswords"))
                .withLastname("Ярец")
                .withFirstname("Илья")
                .withPatronymic("Викторович")
                .withDateOfBirth(LocalDate.of(1995, Month.JULY, 22))
                .withRole(Role.STUDENT);

        Student s2 = new Student()
                .withCredential(new Credential()
                        .withLogin("STOLYAR55")
                        .withPassword("st678"))
                .withLastname("Столярчук")
                .withFirstname("Анастасия")
                .withPatronymic("Ивановна")
                .withDateOfBirth(LocalDate.of(1995, Month.AUGUST, 17))
                .withRole(Role.STUDENT);

        Student s3 = new Student()
                .withCredential(new Credential()
                        .withLogin("BabkaVKedah")
                        .withPassword("worldoftanks"))
                .withLastname("Татур")
                .withFirstname("Егор")
                .withPatronymic("Евгеньевич")
                .withDateOfBirth(LocalDate.of(1995, Month.NOVEMBER, 27))
                .withRole(Role.STUDENT);

        Student s4 = new Student()
                .withCredential(new Credential()
                        .withLogin("Smartdyika")
                        .withPassword("books34"))
                .withLastname("Полошавец")
                .withFirstname("Ксения")
                .withPatronymic("Антоновна")
                .withDateOfBirth(LocalDate.of(1995, Month.APRIL, 10))
                .withRole(Role.STUDENT);

        Student s5 = new Student()
                .withCredential(new Credential()
                        .withLogin("azazello666")
                        .withPassword("tech1"))
                .withLastname("Анпилов")
                .withFirstname("Андрей")
                .withPatronymic("Сергеевич")
                .withDateOfBirth(LocalDate.of(1991, Month.DECEMBER, 11))
                .withRole(Role.STUDENT);

        Student s6 = new Student()
                .withCredential(new Credential()
                        .withLogin("Shabalina_Anzhela")
                        .withPassword("flower4"))
                .withLastname("Шабалина")
                .withFirstname("Анжелина")
                .withPatronymic("Игоревна")
                .withDateOfBirth(LocalDate.of(1995, Month.MARCH, 28))
                .withRole(Role.STUDENT);

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